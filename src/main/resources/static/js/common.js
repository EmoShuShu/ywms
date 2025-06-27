console.log('JavaScript common.js is connected!');
const user = JSON.parse(localStorage.getItem("user") || "{}");
console.log("user: ",user)
if (!user) {
  alert("未登录或会话过期，请重新登录");
  location.href = "login.html";
} else {
  document.getElementById("welcomeText").innerText = `${user.identityName || ""} 欢迎您`;
}

function logout() {
  localStorage.removeItem("user");
  window.location.href = "login.html";
}

async function showReport() {
  document.getElementById("reportModal").style.display = "flex";
  const reportContent = document.getElementById("reportContent");
  reportContent.innerHTML = "加载中...";

  try {
    const res = await fetch(`http:
    let data = await res.json();

    if (!data.success) {
      reportContent.innerHTML = `<p style="color:red;">${data.errormsg || "获取报表失败"}</p>`;
      return;
    }
    data = data.data
    reportContent.innerHTML = `
      <h4>本周</h4>
      <p>发起工单：${data.weekSendReportNumber ?? "暂无"}</p>
      <p>按时完成：${data.weekFinishedReportNumber ?? "暂无"}</p>
      <p>超时完成：${data.weekUnfinishedReportNumber ?? "暂无"}</p>

      <h4>本月</h4>
      <p>发起工单：${data.monthSendReportNumber ?? "暂无"}</p>
      <p>按时完成：${data.monthFinishedReportNumber ?? "暂无"}</p>
      <p>超时完成：${data.monthUnfinishedReportNumber ?? "暂无"}</p>

      <h4>本年</h4>
      <p>发起工单：${data.yearSendReportNumber ?? "暂无"}</p>
      <p>按时完成：${data.yearFinishedReportNumber ?? "暂无"}</p>
      <p>超时完成：${data.yearUnfinishedReportNumber ?? "暂无"}</p>

      <h4>累计</h4>
      <p>发起工单：${data.totalSendReportNumber ?? "暂无"}</p>
      <p>按时完成：${data.totalFinishedReportNumber ?? "暂无"}</p>
      <p>超时完成：${data.totalUnfinishedReportNumber ?? "暂无"}</p>
    `;
  } catch (err) {
    reportContent.innerHTML = `<p style="color:red;">请求失败：${err.message}</p>`;
  }
}

function closeModal() {
  document.getElementById("reportModal").style.display = "none";
}