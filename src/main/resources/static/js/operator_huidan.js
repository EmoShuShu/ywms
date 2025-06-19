console.log('JavaScript operator_huidan.js is connected!');
console.log("user: ", user)
let orders = [];
let currentIndex = 0;
let currentOrder = null;

function getOrderStatusText(status) {
  return {
    "-1": "工单被打回",
    1: "进行区审批",
    2: "进行市审批",
    3: "进行省审批",
    4: "审批通过",
    5: "工单完成",
    6: "工单无法完成"
  }[status] || "未知状态";
}

async function loadOrders() {
  try {
      const res = await fetch("http://localhost:8080/api/workorders/waiting", {
      method: "GET",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ user.userId, user.identityNumber })
    });
    console.log("loadOrders res: ", res)
    const data = await res.json();
    console.log("loadOrders data: ", data)
    if (!data.success) {
      document.getElementById("orderCard").innerText = data.errormsg || "加载失败";
      return;
    }
    orders = data.orders || [];
    if (orders.length === 0) {
      document.getElementById("orderCard").innerText = "暂无工单数据";
    } else {
      showOrder();
    }
  } catch (err) {
    document.getElementById("orderCard").innerText = "加载失败，请稍后重试";
  }
}

//async function loadOrders() {
//  // 模拟后端返回的数据
//  const data = {
//    success: true,
//    orders: [
//      {
//        orderId: '101',
//        issueDescription: "打印机无法连接",
//        orderStatus: 1,
//        applicantName: "张三",
//        applicantId: 1001,
//        applicantIdentity: 1,
//        recipientId: 2001,
//        recipientName: "李四",
//        type: 1,
//        department: 1,
//        sendTime: "2025-06-01 09:00",
//        finishTime: null,
//        deadline: "2025-06-10 17:00"
//      },
//      {
//        orderId: '102',
//        issueDescription: "网络频繁断开",
//        orderStatus: 3,
//        applicantName: "李梅",
//        applicantId: 1002,
//        applicantIdentity: 2,
//        recipientId: 2002,
//        recipientName: "王五",
//        type: 2,
//        department: 2,
//        sendTime: "2025-06-02 10:30",
//        finishTime: null,
//        deadline: "2025-06-12 17:00"
//      },
//      {
//        orderId: '103',
//        issueDescription: "办公椅损坏",
//        orderStatus: 5,
//        applicantName: "赵六",
//        applicantId: 1003,
//        applicantIdentity: 1,
//        recipientId: 2003,
//        recipientName: "陈七",
//        type: 3,
//        department: 3,
//        sendTime: "2025-06-03 14:15",
//        finishTime: "2025-06-04 11:00",
//        deadline: "2025-06-07 17:00"
//      }
//    ]
//  };
//
//  orders = data.orders;
//  if (orders.length === 0) {
//    document.getElementById("orderCard").innerText = "暂无工单数据";
//  } else {
//    showOrder();
//  }
//}

function showOrder() {
  currentOrder = orders[currentIndex];
  if (!currentOrder) return;

  document.getElementById("orderCard").innerHTML = `
    <div class="card">
      <h3>工单编号：${currentOrder.orderId}</h3>
      <p><strong>说明：</strong>${currentOrder.issueDescription || "（无）"}</p>
      <p><strong>状态：</strong>${getOrderStatusText(currentOrder.orderStatus)}</p>
      <p><strong>发起人：</strong>${currentOrder.applicantName || "匿名"}（ID：${currentOrder.applicantId || "未知"}，级别：${["", "区级", "市级", "省级"][currentOrder.applicantIdentity]}）</p>
      <p><strong>接收人：</strong>${currentOrder.recipientName || "未分配"}（ID：${currentOrder.recipientId || "N/A"}）</p>
      <p><strong>类型：</strong>${["", "故障维修", "维护", "后勤缺失"][currentOrder.type]}</p>
      <p><strong>派送部门：</strong>${["", "故障维修部门", "维护部门", "后勤保障部门"][currentOrder.department]}</p>
      <p><strong>提交时间：</strong>${currentOrder.sendTime || "未提供"}</p>
      <p><strong>截止时间：</strong>${currentOrder.deadline || "未提供"}</p>
      <p><strong>完成时间：</strong>${currentOrder.finishTime || "未完成"}</p>
    </div>
  `;
}

async function loadAllData() {
  document.getElementById("orderCard").innerText = "加载中...";
  try {
    await loadOrders();
  } catch (error) {
    console.error("加载失败:", error);
  }
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
function switchForm(formType) {
  // 更新按钮状态
  document.querySelectorAll('.form-switch-button').forEach(button => {
    button.classList.remove('active');
  });
  event.target.classList.add('active');

  // 更新表单显示
  document.querySelectorAll('.form-content').forEach(form => {
    form.classList.remove('active');
  });
  document.getElementById(formType + 'Form').classList.add('active');
}

// 发送回单
async function createOrder(event) {
  event.preventDefault();

  const issueDescription = document.getElementById('issueDescription').value.trim();
  const type = document.getElementById('orderType').value;

  if (!issueDescription || !type) {
    alert("请填写所有必填项！");
    return;
  }

  try {
    const response = await fetch('http://localhost:8080/api/workorders/${orderId}/complete', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({issueDescription: issueDescription,
                            type: parseInt(type, 10),
      })
    });

    const result = await response.json();

    if (result.success) {
      alert("回单创建成功！");
      document.getElementById('createOrderForm').reset();
    } else {
      alert(`创建失败: ${result.errorMsg || '未知错误'}`);
    }
  } catch (error) {
    console.error('Error creating order:', error);
    alert("请求失败，请检查网络或联系管理员。");
  }
}

// 派送工单
async function dispatchOrder(event) {
  event.preventDefault();
  const targetDepartment = document.getElementById('targetDepartment').value;
  if (!targetDepartment) {
    alert("请选择目标部门！");
    return;
  }
  try {
    const response = await fetch('http://localhost:8080/api/workorders/${orderId}/deliver', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({targetDepartment: parseInt(targetDepartment, 10)})
    });
    const result = await response.json();
    if (result.success) {
      alert("工单派送成功！");
      document.getElementById('dispatchOrderForm').reset();
      await loadOrders();
    } else {
      alert(`派送失败: ${result.errorMsg || '未知错误'}`);
    }
  } catch (error) {
    console.error('Error dispatching order:', error);
    alert("请求失败，请检查网络或联系管理员。");
  }
}

document.getElementById('createOrderForm').addEventListener('submit', createOrder);
document.getElementById('dispatchOrderForm').addEventListener('submit', dispatchOrder);