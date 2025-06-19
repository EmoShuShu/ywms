console.log('JavaScript operator_chakan.js is connected!');
console.log("user: ", user)
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
      body: JSON.stringify({ userId: user.userId, identityNumber: user.identityNumber })
    });
    console.log("loadOrders res: ", res)
    const data = await res.json();
    console.log("loadOrders data: ", data)
    if (!data.success) {
      document.getElementById("orderCard").innerText = data.errormsg || "加载失败";
      return;
    }
    orders = data.data || [];
    if (orders.length === 0) {
      document.getElementById("orderCard").innerText = "暂无工单数据";
    } else {
      showOrder();
    }
  } catch (err) {
    document.getElementById("orderCard").innerText = "加载失败，请稍后重试";
  }
}
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

async function loadReceipts() {
  try {
    const res = await fetch('http://localhost:8080/api/operator/check');
    const Data = await res.json();
    console.log("loadReceipts res: ", res)
    console.log("loadReceipts res.json(): ", Data)
    if (!Data.success) {
      document.getElementById("receiptCard").innerText = Data.errormsg || "加载失败";
      return;
    }
    receipts = Data.data || [];
  } catch (err) {
    document.getElementById("receiptCard").innerText = "加载失败，请稍后重试";
  }
}
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