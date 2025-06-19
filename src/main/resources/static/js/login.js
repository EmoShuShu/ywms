console.log('JavaScript login.js is connected!');
function isValidUserId(id) {
  return /^\d{8}$/.test(id);
}
function isValidPassword(pw) {
  return /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{6,20}$/.test(pw);
}
async function login() {
  const userId = document.getElementById("userId").value.trim();
  const password = document.getElementById("password").value.trim();
  if (!isValidUserId(userId)) {
    alert("账号应为8位数字");
    return;
  }
  if (!isValidPassword(password)) {
    alert("密码应为6~20位，包含大写、小写字母和数字");
    return;
  }
  try {
    const res = await fetch("http://localhost:8080/api/users/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ userId, password })
    });
    console.log("res", res)
    if (!res.ok) {
      throw new Error("1账号或密码错误，请重试");
    }
    const data = await res.json();
    const user = data.data;
    console.log("user", user)
    localStorage.setItem("user", JSON.stringify(user));
    if (user.identityNumber === 1) location.href = "applicant_chakan.html";
    else if (user.identityNumber === 2) location.href = "approver_chakan.html";
    else if (user.identityNumber === 3) location.href = "operator_chakan.html";
    else alert("未知身份，禁止登录");
  } catch (err) {
    alert("2账号或密码错误，请重试");
  }
}