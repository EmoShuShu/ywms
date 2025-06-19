console.log('JavaScript approver_shenpi.js is connected!');
let orders = [];
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
//        orderId: 101,
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
//        orderId: 102,
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
//        orderId: 103,
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
      <div class="card-actions"><button onclick="rejectOrder(${order.orderId})">打回工单</button><button onclick="approveOrder(${order.orderId})">通过工单</button></div>
    </div>
  `;
}

async function rejectOrder(orderId) {
  if (!confirm("确定要打回该工单吗？")) return;
  try {
    const res = await fetch(`http://localhost:8080/api/workorders/${orderId}/review`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ false })
    });
    const result = await res.json();
    console.log("rejectOrder res: ", res)
    console.log("rejectOrder data: ", data)
    if (result.success) {
      alert("工单已打回");
      loadOrders();
    } else {
      alert(result.errormsg || "打回失败");
    }
  } catch (err) {
    alert("请求失败，请稍后重试");
  }
}

async function approveOrder(orderId) {
  if (!confirm("确定要通过该工单吗？")) return;
  try {
    const res = await fetch(`http://localhost:8080/api/workorders/${orderId}/review`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ true })
    });
    const result = await res.json();
    if (result.success) {
      alert("工单已通过");
      loadOrders();
    } else {
      alert(result.errormsg || "通过失败");
    }
  } catch (err) {
    alert("请求失败，请稍后重试");
  }
}

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