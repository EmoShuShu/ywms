console.log('JavaScript applicant_chakan.js is connected!');



let orders = [];
let receipts = [];
let currentIndex = 0;


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

function getReceiptStatusText(status) {
    return {
        "1": "已完成",
        "2": "无法完成"
    }[status] || "未知状态";
}

function getDepartmentText(department) {
    return {
        "1": "故障维修部门",
        "2": "维护部门",
        "3": "后勤保障部门"
    }[department] || "未知部门";
}




async function loadOrders() {
    try {
        const url = "http:
        const res = await fetch(url, { method: "GET" });

        if (!res.ok) {
            if (res.status === 401) {
                throw new Error("用户未登录或会话已过期，请重新登录。");
            }
            throw new Error(`服务器错误，状态码: ${res.status}`);
        }

        const responseData = await res.json();

        if (responseData.success) {

            orders = responseData.data || [];

            if (orders.length === 0) {
                document.getElementById("orderCard").innerText = "暂无工单数据";
            } else {
                currentIndex = 0;
                showOrder();
            }
        } else {
            throw new Error(responseData.errorMsg || "后端返回业务失败");
        }

    } catch (err) {
        console.error("加载工单失败 (loadOrders):", err);
        document.getElementById("orderCard").innerText = `加载工单失败: ${err.message}`;
    }
}


async function loadReceipts() {
    try {
        const url = "http:
        const res = await fetch(url, { method: "GET" });

        if (!res.ok) {
            if (res.status === 401) throw new Error("用户未登录或会话已过期。");
            throw new Error(`服务器错误，状态码: ${res.status}`);
        }

        const responseData = await res.json();

        if (responseData.success) {
            receipts = responseData.data || [];
        } else {
            console.warn("加载回单业务失败:", responseData.errorMsg);
            receipts = [];
        }

    } catch (err) {
        console.error("加载回单失败 (loadReceipts):", err);

        document.getElementById("receiptCard").innerText = `加载回单失败: ${err.message}`;
    }
}




function showOrder() {
    if (orders.length === 0 || !orders[currentIndex]) {
        document.getElementById("orderCard").innerText = "暂无工单数据";
        document.getElementById("receiptCard").innerText = "";
        return;
    }

    const order = orders[currentIndex];

    document.getElementById("orderCard").innerHTML = `
    <div class="card">
      <h3>工单编号：${order.orderId}</h3>
      <p><strong>说明：</strong>${order.issueDescription || "（无）"}</p>
      <p><strong>状态：</strong>${getOrderStatusText(order.orderStatus)}</p>
      <p><strong>发起人：</strong>${order.applicantName || "匿名"}（ID：${order.applicantId || "未知"}，级别：${["", "区级", "市级", "省级"][order.applicantIdentity] || "未知"}）</p>
      <p><strong>接收人：</strong>${order.recipientName || "未分配"}（ID：${order.recipientId || "N/A"}）</p>
      <p><strong>类型：</strong>${["", "故障维修", "维护", "后勤缺失"][order.type] || "未知"}</p>
      <p><strong>派送部门：</strong>${getDepartmentText(order.department)}</p>
      <p><strong>提交时间：</strong>${order.sendTime || "未提供"}</p>
      <p><strong>截止时间：</strong>${order.deadline || "未提供"}</p>
      <p><strong>完成时间：</strong>${order.finishTime || "未完成"}</p>
      ${
        [-1, 1, 2, 3, 4].includes(order.orderStatus)
          ? `<div class="card-actions"><button onclick="cancelOrder('${order.orderId}')">撤回工单</button></div>`
          : ''
      }
    </div>
  `;


    const associatedReceipt = receipts.find(receipt => receipt.workOrderId === order.orderId);

    if (associatedReceipt) {
        showReceipt(associatedReceipt);
    } else {
        document.getElementById("receiptCard").innerText = "此工单暂无回单数据";
    }
}


function showReceipt(receipt) {
    if (!receipt) return;

    document.getElementById("receiptCard").innerHTML = `
    <div class="card">
      <h3>回单编号：${receipt.responseId}</h3>
      <p><strong>关联工单ID：</strong>${receipt.workOrderId}</p>
      <p><strong>回单状态：</strong>${getReceiptStatusText(receipt.responseStatus)}</p>
      <p><strong>回单描述：</strong>${receipt.responseDescription || "（无）"}</p>
      <p><strong>操作人员：</strong>${receipt.operatorName || "未知"} (ID: ${receipt.responseUserId || "N/A"})</p>
      <p><strong>操作部门：</strong>${getDepartmentText(receipt.responseDepartment)}</p>
    </div>
  `;
}


async function cancelOrder(orderId) {
    if (!confirm(`确定要撤回工单 ${orderId} 吗？`)) return;

    try {
        const res = await fetch(`http:
            method: "DELETE"
        });

        if (!res.ok) {
            if (res.status === 401) throw new Error("用户未登录或会话已过期。");
            throw new Error(`服务器响应: ${res.status}`);
        }

        const result = await res.json();
        if (result.success) {
            alert("工单已成功撤回");
            loadAllData();
        } else {
            throw new Error(result.errorMsg || "撤回失败，但未提供原因");
        }
    } catch (err) {
        console.error("撤回工单失败 (cancelOrder):", err);
        alert(`撤回失败: ${err.message}`);
    }
}





async function loadAllData() {
    document.getElementById("orderCard").innerText = "加载中...";
    document.getElementById("receiptCard").innerText = "加载中...";


    await loadReceipts();
    await loadOrders();

    console.log("所有数据加载完成。");
}


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


loadAllData();