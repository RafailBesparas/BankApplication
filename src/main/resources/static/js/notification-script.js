let allNotifications = [];

document.addEventListener('DOMContentLoaded', fetchNotifications);

function fetchNotifications() {
  fetch('/notifications')
    .then(res => res.json())
    .then(data => {
      allNotifications = data;
      renderNotifications(data);
    });
}

function renderNotifications(notifications) {
  const container = document.getElementById('notifications');
  container.innerHTML = '';

  if (notifications.length === 0) {
    container.innerHTML = '<p>No notifications found.</p>';
    return;
  }

  notifications.forEach(n => {
    const card = document.createElement('div');
    card.className = 'notification-card';

    const content = document.createElement('div');
    content.innerHTML = `
      <div class="priority ${n.priority}">${n.priority}</div>
      <strong>${n.message}</strong>
      <div class="meta">${timeAgo(n.timestamp)} &bull; ${n.type}</div>
    `;

    const button = document.createElement('button');
    button.className = 'read-button';
    button.textContent = n.read ? 'Read' : 'Mark as Read';
    if (n.read) button.disabled = true;

    button.onclick = () => markAsRead(n.id, button);

    card.appendChild(content);
    card.appendChild(button);
    container.appendChild(card);
  });
}

function markAsRead(id, button) {
  fetch(`/notifications/${id}/read`, { method: 'PATCH' })
    .then(() => {
      button.textContent = 'Read';
      button.disabled = true;
    });
}

function filterType(type) {
  if (type === 'ALL') {
    renderNotifications(allNotifications);
  } else {
    const filtered = allNotifications.filter(n => n.type === type);
    renderNotifications(filtered);
  }
}

function timeAgo(dateTimeStr) {
  const date = new Date(dateTimeStr);
  const seconds = Math.floor((new Date() - date) / 1000);

  const rtf = new Intl.RelativeTimeFormat('en', { numeric: 'auto' });
  if (seconds < 60) return rtf.format(-seconds, 'seconds');
  const minutes = Math.floor(seconds / 60);
  if (minutes < 60) return rtf.format(-minutes, 'minutes');
  const hours = Math.floor(minutes / 60);
  if (hours < 24) return rtf.format(-hours, 'hours');
  const days = Math.floor(hours / 24);
  return rtf.format(-days, 'days');
}
