document.addEventListener('DOMContentLoaded', () => {
    const monthlyData = window.monthlySpending;
    const categoryData = window.categorySpending;

    const months = Object.keys(monthlyData);
    const amounts = Object.values(monthlyData);

    const categories = Object.keys(categoryData);
    const catAmounts = Object.values(categoryData);

    new Chart(document.getElementById('lineChart'), {
        type: 'line',
        data: {
            labels: months,
            datasets: [{
                label: 'Monthly Spend',
                data: amounts,
                borderColor: '#0d6efd',
                backgroundColor: 'rgba(13,110,253,0.1)',
                fill: true,
                tension: 0.4
            }]
        },
        options: {
            responsive: true,
            plugins: {
                tooltip: { mode: 'index', intersect: false },
                legend: { display: false }
            },
            interaction: { mode: 'nearest', axis: 'x', intersect: false }
        }
    });

    new Chart(document.getElementById('barChart'), {
        type: 'bar',
        data: {
            labels: categories,
            datasets: [{
                label: 'Category Spend',
                data: catAmounts,
                backgroundColor: '#ffc107'
            }]
        },
        options: {
            responsive: true,
            plugins: {
                tooltip: { mode: 'index', intersect: false },
                legend: { display: false }
            }
        }
    });
});
