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
    const res = await fetch("http://localhost:8080/api/workorders", {
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
  const order = orders[currentIndex];
  if (!order) return;

  document.getElementById("orderCard").innerHTML = `
    <div class="card">
      <h3>工单编号：${order.orderId}</h3>
      <p><strong>说明：</strong>${order.issueDescription || "（无）"}</p>
      <p><strong>状态：</strong>${getOrderStatusText(order.orderStatus)}</p>
      <p><strong>发起人：</strong>${order.applicantName || "匿名"}（ID：${order.applicantId || "未知"}，级别：${["", "区级", "市级", "省级"][order.applicantIdentity]}）</p>
      <p><strong>接收人：</strong>${order.recipientName || "未分配"}（ID：${order.recipientId || "N/A"}）</p>
      <p><strong>类型：</strong>${["", "故障维修", "维护", "后勤缺失"][order.type]}</p>
      <p><strong>派送部门：</strong>${["", "故障维修部门", "维护部门", "后勤保障部门"][order.department]}</p>
      <p><strong>提交时间：</strong>${order.sendTime || "未提供"}</p>
      <p><strong>截止时间：</strong>${order.deadline || "未提供"}</p>
      <p><strong>完成时间：</strong>${order.finishTime || "未完成"}</p>
      ${
        [-1,1,2,3,4].includes(order.orderStatus)
          ? `<div class="card-actions"><button onclick="cancelOrder(${order.orderId})">撤回工单</button></div>`
          : ''
      }
    </div>
  `;
  let flag = -1;
  const workOrderId = orders[currentIndex].orderId;
  for (let i = 0; i < receipts.length; i++){
    if (receipts[i].workOrderId === workOrderId){
      flag = i;
      break;
    }
  }
  if (flag > -1) {
    showReceipt(flag)
  }
  else {
    document.getElementById("receiptCard").innerText = "暂无回单数据";
  }
}

async function cancelOrder(orderId) {
  if (!confirm("确定要撤回该工单吗？")) return;
  try {
    const res = await fetch(`http://localhost:8080/api/workorders/${orderId}`, {
      method: "DELETE",
      headers: { "Content-Type": "application/json" }
    });
    const result = await res.json();
    console.log("cencelOrder res: ", res)
    console.log("cencelOrder result: ", result)
    if (result.success) {
      alert("工单已撤回");
      loadOrders();
    } else {
      alert(result.errormsg || "撤回失败");
    }
  } catch (err) {
    alert("请求失败，请稍后重试");
  }
}

async function loadReceipts() {
  try {
    const res = await fetch("http://localhost:8080/api/workorders/check", {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    });
    const data = await res.json();
    console.log("res: ", res)
    console.log("data: ", data)
    if (!data.success) {
      document.getElementById("receiptCard").innerText = data.errormsg || "加载失败";
      return;
    }
    receipts = data.receipts || [];
    if (receipts.length === 0) {
      document.getElementById("receiptCard").innerText = "暂无回单数据";
    } else {
      showReceipt();
    }
  } catch (err) {
    document.getElementById("receiptCard").innerText = "加载失败，请稍后重试";
  }
}
//async function loadReceipts() {
//  try {
//    const mockResponse = {
//      success: true,
//      data: [
//        {
//          responseId: "R-2025-003",
//          responseDescription: "网络端口已重置，交换机配置已调整，目前连接稳定。",
//          responseStatus: "1",
//          responseUserId: 2002,
//          operatorName: "王五",
//          responseDepartment: 2,
//          workOrderId: "102"
//        },
//        {
//          responseId: "R-2025-002",
//          responseDescription: "经检查，办公椅支架断裂，无法修复，建议更换。",
//          responseStatus: "2",
//          responseUserId: 2003,
//          operatorName: "陈七",
//          responseDepartment: 3,
//          workOrderId: "103"
//        },
//      ],
//      errorMsg: null
//    };
//
//    if (!mockResponse.success) {
//      document.getElementById("receiptCard").innerText = mockResponse.errorMsg || "加载失败";
//      return;
//    }
//
//    receipts = mockResponse.data || [];
//  } catch (err) {
//    document.getElementById("receiptCard").innerText = "加载失败，请稍后重试";
//  }
//}

function showReceipt(Index) {
  const receipt = receipts[Index];
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

async function loadAllData() {
  document.getElementById("orderCard").innerText = "加载中...";
  document.getElementById("receiptCard").innerText = "加载中...";

  try {
    await loadReceipts();
    await loadOrders();
  } catch (error) {
    console.error("加载失败:", error);
  }
}
loadAllData()
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