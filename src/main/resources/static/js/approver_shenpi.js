console.log('JavaScript approver_shenpi.js is connected!');
// 在脚本开始时打印user，用于调试
console.log("user at script start: ", typeof user !== 'undefined' ? user : 'user is not defined yet');

// --- 全局变量声明 ---
let orders = [];
let currentIndex = 0;

// --- 辅助函数 ---
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


// --- 数据加载函数 (核心修正) ---

/**
 * 从后端加载待审批的工单列表。
 * 适配 Session-Cookie 认证。
 */
async function loadOrders() {
    try {
        const url = "http://localhost:8080/api/workorders/waiting";
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
                document.getElementById("orderCard").innerText = "暂无需要您审批的工单";
            } else {
                currentIndex = 0;
                showOrder();
            }
        } else {
            throw new Error(responseData.errorMsg || "后端返回业务失败");
        }
    } catch (err) {
        console.error("加载待审批工单失败 (loadOrders):", err);
        document.getElementById("orderCard").innerText = `加载失败: ${err.message}`;
    }
}


// --- UI 显示与交互函数 ---

function showOrder() {
    if (orders.length === 0 || !orders[currentIndex]) {
        document.getElementById("orderCard").innerText = "暂无需要您审批的工单";
        return;
    }
    const order = orders[currentIndex];

    document.getElementById("orderCard").innerHTML = `
    <div class="card">
      <h3>工单编号：${order.orderId}</h3>
      <p><strong>说明：</strong>${order.issueDescription || "（无）"}</p>
      <p><strong>状态：</strong>${getOrderStatusText(order.orderStatus)}</p>
      <p><strong>发起人：</strong>${order.applicantName || "匿名"}（ID：${order.applicantId || "未知"}，级别：${["", "区级", "市级", "省级"][order.applicantIdentity] || '未知'}）</p>
      <p><strong>接收人：</strong>${order.recipientName || "未分配"}（ID：${order.recipientId || "N/A"}）</p>
      <p><strong>类型：</strong>${["", "故障维修", "维护", "后勤缺失"][order.type] || '未知'}</p>
      <p><strong>派送部门：</strong>${["", "故障维修部门", "维护部门", "后勤保障部门"][order.department] || '未知'}</p>
      <p><strong>提交时间：</strong>${order.sendTime || "未提供"}</p>
      <p><strong>截止时间：</strong>${order.deadline || "未提供"}</p>
      <p><strong>完成时间：</strong>${order.finishTime || "未完成"}</p>
      <div class="card-actions">
        <button onclick="reviewOrder('${order.orderId}', false)">打回工单</button>
        <button onclick="reviewOrder('${order.orderId}', true)">通过工单</button>
      </div>
    </div>
  `;
}


/**
 * 统一处理审批操作（通过或打回）。
 * @param {string} orderId - 要审批的工单ID。
 * @param {boolean} isApproved - true表示通过，false表示打回。
 */
async function reviewOrder(orderId, isApproved) {
    const actionText = isApproved ? "通过" : "打回";
    if (!confirm(`确定要${actionText}该工单吗？`)) return;

    try {
        // 【修正】使用反引号 ` (模板字符串) 来构造URL
        const url = `http://localhost:8080/api/workorders/${orderId}/review`;
        const res = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ approved: isApproved })
        });

        if (!res.ok) {
            if (res.status === 401) throw new Error("用户未登录或会话已过期。");
            throw new Error(`服务器响应错误: ${res.status}`);
        }

        const result = await res.json();
        if (result.success) {
            alert(`工单已${actionText}`);
            // 刷新列表以移除已处理的工单
            loadOrders();
        } else {
            throw new Error(result.errorMsg || `${actionText}失败`);
        }
    } catch (err) {
        console.error(`${actionText}工单失败 (reviewOrder):`, err);
        alert(`请求失败: ${err.message}`);
    }
}


// --- 流程控制与初始化 ---

// 页面加载时自动加载待审批工单
loadOrders();

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

// 报表相关的函数 (从HTML中看到，这里补充定义)
function showReport() {
    document.getElementById('reportModal').style.display = 'block';
    // 在这里可以添加加载报表数据的逻辑
}

function closeModal() {
    document.getElementById('reportModal').style.display = 'none';
}