// Global array to store all notifications fetched from the backend
let allNotifications = [];

// When the DOM is fully loaded, start fetching notifications
document.addEventListener('DOMContentLoaded', fetchNotifications);

// Fetch notifications from the backend and render them
function fetchNotifications() {
  fetch('/notifications') // Assumes endpoint returns JSON array of notifications
    .then(res => res.json()) // Parse the JSON response
    .then(data => {
      allNotifications = data; // Store data globally for filtering
      renderNotifications(data); // Render them on the page
    });
}

// Renders an array of notifications to the HTML
function renderNotifications(notifications) {
  const container = document.getElementById('notifications'); // Target container
  container.innerHTML = '';// Clear previous content

  // Handle empty state
  if (notifications.length === 0) {
    container.innerHTML = '<p>No notifications found.</p>';
    return;
  }

  // Render each notification as a card
  notifications.forEach(n => {
    const card = document.createElement('div');
    card.className = 'notification-card'; // Use for CSS styling

    const content = document.createElement('div');
    content.innerHTML = `
      <div class="priority ${n.priority}">${n.priority}</div>
      <strong>${n.message}</strong>
      <div class="meta">${timeAgo(n.timestamp)} &bull; ${n.type}</div>
    `;

    const button = document.createElement('button');
    button.className = 'read-button'; // Style class
    button.textContent = n.read ? 'Read' : 'Mark as Read'; // Button label
    if (n.read) button.disabled = true; // Disable if already read

// Mark as read on click
    button.onclick = () => markAsRead(n.id, button);

    card.appendChild(content); // Add content to card
    card.appendChild(button); // Add button to card
    container.appendChild(card); // Add card to container
  });
}

// Sends a PATCH request to mark a specific notification as read
function markAsRead(id, button) {
  fetch(`/notifications/${id}/read`, { method: 'PATCH' })
    .then(() => {
      button.textContent = 'Read'; // Update button label
      button.disabled = true; // Disable button to prevent re-click
    });
}

// Filters notifications by type (e.g., SECURITY, TRANSACTION)
function filterType(type) {
  if (type === 'ALL') {
    renderNotifications(allNotifications); // Show all
  } else {
    const filtered = allNotifications.filter(n => n.type === type);
    renderNotifications(filtered); // Show filtered
  }
}

// Converts ISO timestamp to human-friendly "time ago" format using Intl.RelativeTimeFormat
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
