console.log('JavaScript operator_huidan.js is connected!');
console.log("user: ", user);

// --- 全局变量声明 ---
let orders = [];
let currentIndex = 0;
let currentOrder = null; // 用于存储当前显示的工单对象

// --- 辅助函数 ---
function getOrderStatusText(status) {
    return { "-1": "工单被打回", 1: "进行区审批", 2: "进行市审批", 3: "进行省审批", 4: "审批通过", 5: "工单完成", 6: "工单无法完成" }[status] || "未知状态";
}


// --- 数据加载函数 (核心修正) ---
async function loadOrders() {
    try {
        // 【修正】这里需要一个专门为操作员获取已分配工单的接口URL
        // 我暂时用一个占位符，请根据你的后端Controller进行替换
        const url = "http://localhost:8080/api/workorders/waiting"; // <--- !! 请务必确认并修改为正确的URL !!

        // 【修正】GET请求，适配Session-Cookie，无需任何 headers 或 body
        const res = await fetch(url, { method: "GET" });

        if (!res.ok) {
            if (res.status === 401) throw new Error("用户未登录或会话已过期。");
            throw new Error(`服务器错误，状态码: ${res.status}`);
        }

        const responseData = await res.json();
        console.log("loadOrders responseData:", responseData);

        if (responseData.success) {
            orders = responseData.data || [];
            if (orders.length === 0) {
                document.getElementById("orderCard").innerText = "暂无需要您处理的工单";
                // 没有工单时，清空表单区域
                const formsContainer = document.getElementById('formsContainer');
                if (formsContainer) formsContainer.style.display = 'none';
            } else {
                currentIndex = 0;
                showOrder();
                // 有工单时，显示表单区域
                const formsContainer = document.getElementById('formsContainer');
                if (formsContainer) formsContainer.style.display = 'block';
            }
        } else {
            throw new Error(responseData.errorMsg || "后端返回业务失败");
        }
    } catch (err) {
        console.error("加载工单失败 (loadOrders):", err);
        document.getElementById("orderCard").innerText = `加载失败: ${err.message}`;
    }
}


// --- UI 显示与交互函数 ---
function showOrder() {
    currentOrder = orders[currentIndex];
    if (!currentOrder) {
        document.getElementById("orderCard").innerText = "暂无需要您处理的工单";
        const formsContainer = document.getElementById('formsContainer');
        if (formsContainer) formsContainer.style.display = 'none';
        return;
    }

    document.getElementById("orderCard").innerHTML = `
    <div class="card">
      <h3>工单编号：${currentOrder.orderId}</h3>
      <p><strong>说明：</strong>${currentOrder.issueDescription || "（无）"}</p>
      <p><strong>状态：</strong>${getOrderStatusText(currentOrder.orderStatus)}</p>
      <p><strong>发起人：</strong>${currentOrder.applicantName || "匿名"}（ID：${currentOrder.applicantId || "未知"}，级别：${["", "区级", "市级", "省级"][currentOrder.applicantIdentity] || '未知'}）</p>
      <p><strong>接收人：</strong>${currentOrder.recipientName || "未分配"}（ID：${currentOrder.recipientId || "N/A"}）</p>
      <p><strong>类型：</strong>${["", "故障维修", "维护", "后勤缺失"][currentOrder.type] || '未知'}</p>
      <p><strong>派送部门：</strong>${["", "故障维修部门", "维护部门", "后勤保障部门"][currentOrder.department] || '未知'}</p>
      <p><strong>提交时间：</strong>${currentOrder.sendTime || "未提供"}</p>
      <p><strong>截止时间：</strong>${currentOrder.deadline || "未提供"}</p>
      <p><strong>完成时间：</strong>${currentOrder.finishTime || "未完成"}</p>
    </div>
  `;
}


// --- 表单提交处理 (保留原函数名) ---

// 发送回单
async function createOrder(event) {
  event.preventDefault();
  if (!currentOrder) {
    alert("没有选中的工单，无法发送回单。");
    return;
  }

  // 【修正】这里的ID应该从HTML表单中获取
  const issueDescription = document.getElementById('receiptDescription').value.trim(); // 假设回单描述的ID是这个
  const type = document.getElementById('receiptStatus').value; // 假设回单状态的ID是这个

  if (!issueDescription || !type) {
    alert("请填写所有必填项！");
    return;
  }

  try {
    // 【修正】使用反引号 ` 和正确的工单ID变量
    const url = `http://localhost:8080/api/workorders/${currentOrder.orderId}/complete`;
    const response = await fetch(url, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({ responseDescription: issueDescription, responseStatus: type })
    });

    if (!response.ok) throw new Error(`服务器响应错误: ${response.status}`);

    const result = await response.json();
    if (result.success) {
      alert("回单创建成功！");
      document.getElementById('createOrderForm').reset();
      loadOrders();
    } else {
      throw new Error(result.errorMsg || '创建失败');
    }
  } catch (error) {
    console.error('创建回单失败:', error);
    alert(`请求失败: ${error.message}`);
  }
}

// 派送工单
async function dispatchOrder(event) {
  event.preventDefault();
  if (!currentOrder) {
    alert("没有选中的工单，无法派送。");
    return;
  }

  const targetDepartment = document.getElementById('targetDepartment').value;
  if (!targetDepartment) {
    alert("请选择目标部门！");
    return;
  }

  try {
    // 【修正】使用反引号 ` 和正确的工单ID变量
    const url = `http://localhost:8080/api/workorders/${currentOrder.orderId}/deliver`;
    const response = await fetch(url, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({ departmentId: parseInt(targetDepartment, 10) })
    });

    if (!response.ok) throw new Error(`服务器响应错误: ${response.status}`);

    const result = await response.json();
    if (result.success) {
      alert("工单派送成功！");
      document.getElementById('dispatchOrderForm').reset();
      loadOrders();
    } else {
      throw new Error(result.errorMsg || '派送失败');
    }
  } catch (error) {
    console.error('派送工单失败:', error);
    alert(`请求失败: ${error.message}`);
  }
}


// --- 流程控制与初始化 ---
function loadAllData() {
  document.getElementById("orderCard").innerText = "加载中...";
  loadOrders();
}

loadAllData();

function prevOrder() {
  if (currentIndex > 0) {
    currentIndex--;
    showOrder();
  }
}

function nextOrder() {
  if (currentIndex < orders.length - 1) {
    currentIndex++;
    showOrder();
  }
}

// 切换表单显示
function switchForm(formType, buttonElement) {
  document.querySelectorAll('.form-switch-button').forEach(button => {
    button.classList.remove('active');
  });
  // 使用 event.target 可能会有问题，如果函数不是直接由事件触发
  // 改为传递按钮元素本身更稳妥
  buttonElement.classList.add('active');

  document.querySelectorAll('.form-content').forEach(form => {
    form.style.display = 'none';
  });
  document.getElementById(formType + 'Form').style.display = 'block';
}

// 事件绑定（使用原有的HTML onsubmit方式，所以这里不需要JS绑定）
// document.getElementById('createOrderForm').addEventListener('submit', createOrder);
// document.getElementById('dispatchOrderForm').addEventListener('submit', dispatchOrder);