app.controller('ChartController', function ($scope) {
    $scope.dates = [
        ['2023-01-01', 100],
        ['2023-02-01', 41],
        ['2023-03-01', 35],
        ['2023-04-01', 101],
        ['2023-05-01', 495],
        ['2023-06-01', 62],
        ['2023-07-01', 69],
        ['2023-08-01', 391],
        ['2023-09-01', 248],
        ['2023-10-01', 248]
      ];
      
      $scope.options = {
        series: [{
          name: 'XYZ MOTORS',
          data: $scope.dates
        }],
        chart: {
          type: 'area',
          stacked: false,
          height: 350,
          zoom: {
            type: 'x',
            enabled: true,
            autoScaleYaxis: true
          },
          toolbar: {
            autoSelected: 'zoom'
          }
        },
        dataLabels: {
          enabled: false
        },
        markers: {
          size: 0,
        },
        title: {
          text: 'Stock Price Movement',
          align: 'left'
        },
        fill: {
          type: 'gradient',
          gradient: {
            shadeIntensity: 1,
            inverseColors: false,
            opacityFrom: 0.5,
            opacityTo: 0,
            stops: [0, 90, 100]
          },
        },
        yaxis: {
          labels: {
            formatter: function (val) {
              return (val / 1000000).toFixed(0);
            },
          },
          title: {
            text: 'Price'
          },
        },
        xaxis: {
          type: 'datetime',
        },
        tooltip: {
          shared: false,
          y: {
            formatter: function (val) {
              return (val / 1000000).toFixed(0)
            }
          }
        }
      };
      
      var chart = new ApexCharts(document.querySelector("#chart1"), $scope.options);
      chart.render();

    $scope.options2 = {
        series: [{
            name: "Desktops",
            data: [10, 41, 35, 51, 49, 62, 69, 91, 148]
        }],
        chart: {
            height: 350,
            type: 'line',
            zoom: {
                enabled: false
            }
        },
        dataLabels: {
            enabled: false
        },
        stroke: {
            curve: 'straight'
        },
        title: {
            text: 'Product Trends by Month',
            align: 'left'
        },
        grid: {
            row: {
                colors: ['#f3f3f3', 'transparent'],
                opacity: 0.5
            }
        },
        xaxis: {
            categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep']
        }
    };

    var chart2 = new ApexCharts(document.querySelector("#chart2"), $scope.options2);
    chart2.render();
});