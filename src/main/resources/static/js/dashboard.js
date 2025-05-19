// Wait until the DOM is fully loaded before running the chart setup
document.addEventListener('DOMContentLoaded', () => {
    // Access preloaded monthly spending data (expected to be injected by backend or another script)
    const monthlyData = window.monthlySpending;
    // Access preloaded category-wise spending data
    const categoryData = window.categorySpending;

    // Extract the list of months (e.g., ['2024-01', '2024-02', ...])
    const months = Object.keys(monthlyData);
    // Extract corresponding spending amounts for each month
    const amounts = Object.values(monthlyData);

    // Extract the list of categories (e.g., ['Transfer Out', 'Withdrawal', ...])
    const categories = Object.keys(categoryData);
    // Extract corresponding spend amounts for each category
    const catAmounts = Object.values(categoryData);

    // ------------------ Line Chart: Monthly Spending ------------------

    new Chart(document.getElementById('lineChart'), {
        type: 'line', // Specifies that we are creating a line chart
        data: {
            labels: months, // X-axis: month labels
            datasets: [{
                label: 'Monthly Spend', // Name shown in tooltip
                data: amounts, // Y-axis: spending values
                borderColor: '#0d6efd',  // Blue line color
                backgroundColor: 'rgba(13,110,253,0.1)', // Light blue fill below the line
                fill: true, // Fill the area under the line
                tension: 0.4 // Smooth out the line curve
            }]
        },
        options: {
            responsive: true,  // Make chart scale properly with screen size
            plugins: {
                tooltip: { mode: 'index', intersect: false }, // Tooltip shows all values on same X
                legend: { display: false }  // Hide legend for minimal UI
            },
            // Interact with nearest X value
            // Lock interaction to X-axis
            // Don't require cursor to intersect line
            interaction: { mode: 'nearest', axis: 'x', intersect: false }
        }
    });

    new Chart(document.getElementById('barChart'), {
        type: 'bar', // Specifies that we are creating a bar chart
        data: {
            labels: categories, // X-axis: transaction categories
            datasets: [{
                label: 'Category Spend', // Name shown in tooltip
                data: catAmounts,  // Y-axis: total per category
                backgroundColor: '#ffc107' // Amber/yellow bar color
            }]
        },
        options: {
            responsive: true, // Make chart responsive
            plugins: {
                tooltip: { mode: 'index', intersect: false },  // Enable full tooltip
                legend: { display: false } // Hide legend for cleaner UI
            }
        }
    });
});
