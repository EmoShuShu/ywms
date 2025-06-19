function setMinDeadlineDate() {
  const deadlineInput = document.getElementById('deadline');
  const tomorrow = new Date();
  tomorrow.setDate(tomorrow.getDate() + 1);

  const yyyy = tomorrow.getFullYear();
  const mm = String(tomorrow.getMonth() + 1).padStart(2, '0');
  const dd = String(tomorrow.getDate()).padStart(2, '0');

  const minDate = `${yyyy}-${mm}-${dd}`;
  deadlineInput.min = minDate;
}

async function createOrder(event) {
  event.preventDefault();

  const issueDescription = document.getElementById('issueDescription').value.trim();
  const type = document.getElementById('orderType').value;
  const deadline = document.getElementById('deadline').value;
  if (deadlineInput) {
      const deadline = deadlineInput + "T23:59:59";
      console.log(deadline);
  }

  if (!issueDescription || !type || !deadline) {
      alert("请填写所有必填项！");
      return;
  }

  try {
      const response = await fetch('http://localhost:8080/api/workorders', {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json',
          },
          body: {
              issueDescription: issueDescription,
              type: parseInt(type, 10),
              deadline: deadline,
          }
      });

      const result = await response.json();

    if (result.success) {
      alert("工单创建成功！");
      document.getElementById('createOrderForm').reset();
      setMinDeadlineDate();
    } else {
      alert(`创建失败: ${result.errorMsg || '未知错误'}`);
    }
  } catch (error) {
    console.error('Error creating order:', error);
    alert("请求失败，请检查网络或联系管理员。");
  }
}

document.getElementById('createOrderForm').addEventListener('submit', createOrder);

window.onload = function() {
  setMinDeadlineDate();
};