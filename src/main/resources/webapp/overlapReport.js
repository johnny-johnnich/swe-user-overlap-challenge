(function () {
var chartData = [];
var ctx = document.getElementById("myChart");
var myChart = new Chart(ctx, {
  type: 'pie',
  data: {
    labels: ['Total Users', 'Overlap Users'],
    datasets: [{
      data: chartData,
      backgroundColor: [
        'rgba(54,162,235,1)',
        'rgba(255,205,86,1)'
      ],
      borderColor: [
        'rgba(0,0,0,0.1)',
        'rgba(0,0,0,0.1)'
      ],
      borderWidth: 1
    }]
  },
  options: {
   	cutoutPercentage: 40,
    responsive: false,

  }
});
$(".dropdown-item").click( function() {
    var reportTypeStr = $(this).text();
    var reportType;
    switch(reportTypeStr) {
      case 'All':
        reportType = 'ALL';
        break;
      case 'unseen.dw <-> pseudopolis.dw':
        reportType = 'UNSEEN_PSEUDOPOLIS';
        break;
      case 'unseen.dw <-> mended-drum.dw':
        reportType = 'UNSEEN_MENDEDDRUM';
        break;
      case 'pseudopolis.dw <-> mended-drum.dw':
        reportType = 'PSEUDOPOLIS_MENDEDDRUM';
        break;
      default:
        reportType = 'ALL';
    }
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/report/overlapUsers/' + reportType,
        dataType: 'json',
        success: function(data) {
            var reportName = "User Overlap Report: " + reportTypeStr;
            $("#reportName").empty().append(reportName);
            chartData = [data.totalUserCount , data.overlapUserCount];
            $("#percentageText").empty().append(data.overlapPercentage);
            myChart.data.datasets.forEach((dataset) => {
                dataset.data = chartData;
            });
            myChart.update();
        }
    });
});
})();