var host = "http://localhost:8080/api";
app.controller('analysisCtrl', function ($scope, $http, graphqlService, $routeParams, $filter,$cookies) {
    $('#myTab a').click(function (e) {
        e.preventDefault()
        $(this).tab('show')
    })

    if($cookies.get('token')){

    }else{
        window.location.href="/signin"
    }

    $scope.selectedTime = 7;
    $scope.currentDate = $filter('date')(new Date(), 'yyyy-MM-dd');
    $scope.daysAgo = new Date(Date.now() - $scope.selectedTime * 24 * 60 * 60 * 1000);
    $scope.listCountries = [];
    $scope.listRecord = [];
    $scope.listListener = [];
    $scope.listened = 0;
    var aCountries = [];
    var aAge;
    var aGender = [];
    $scope.Recording = function () {
        const query = `{
                recordingById(recordingId: ${$scope.recordingId}) {
                    recordingId
                    recordingName
                    listened
                    monitors{
                      account{
                        username
                        birthday
                        gender
                        country
                      }
                    }
                }
            }`
        graphqlService.executeQuery(query).then(data => {
            $scope.monitor = data.recordingById.monitors;
            $scope.listened = data.recordingById.listened;
            $scope.analysicAge([$scope.recordingId]);
            $scope.analysicGender([$scope.recordingId]);
            $scope.analysicCountry([$scope.recordingId]);
            $scope.newListener();
        }).catch(err => {

        })
    }

    const ctx = $('#chartFollower');
    var chartFollow;
    const age = $('#age');
    var chartAge;
    const record = $('#record');
    var chartRecord;
    const gender = $('#gender');
    var chartGender;
    var mapChart;
    
    $scope.monitorFollow = function () {
        $http.get(host + "/v1/profile", {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            let url = host + "/v1/monitor-follow";
            $http.get(url, {
                params: { email: resp.data.data.account.email, role: 2, duration: $scope.selectedTime }
            }).then(respChart => {
                var listDate = [];
                var listQuantity = [];
                respChart.data.data.forEach(item => {
                    listDate.push($filter('date')(new Date(item[0]), 'yyyy-MM-dd'));
                    listQuantity.push(item[1])
                })
                if (chartFollow) {
                    chartFollow.data.labels = listDate;
                    chartFollow.data.datasets[0].data = listQuantity;
                    chartFollow.update();
                } else {
                    chartFollow = new Chart(ctx, {
                        type: 'line',
                        data: {
                            labels: listDate,
                            datasets: [{
                                data: listQuantity,
                                borderWidth: 1,
                                fill: true,
                                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                                borderColor: 'rgba(75, 192, 192, 1)',
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            scales: {
                                y: {
                                    beginAtZero: true,
                                }
                            },
                            plugins: {
                                legend: {
                                    display: false,
                                },
                            }
                        }
                    });
                }
            })
        }).catch(error => {
            console.log("Not found artist profile")
        })
    }

    $scope.analysicAge = function (listId) {
        $http.get(host + '/v1/monitor/age', {
            params: { id: listId, duration: $scope.selectedTime }
        }).then(resp => {
            var under18 = 0;
            var between18To22 = 0;
            var between23To27 = 0;
            var between28To34 = 0;
            var between35To44 = 0;
            var between45To60 = 0;
            var over60 = 0;
            resp.data.data.forEach(e => {
                if (e.age < 18) {
                    under18 = e.quantity;
                } else if (18 < e.age < 22) {
                    between18To22 = e.quantity;
                } else if (23 < e.age < 27) {
                    between23To27 = e.quantity;
                } else if (28 < e.age < 34) {
                    between28To34 = e.quantity;
                } else if (35 < e.age < 44) {
                    between35To44 = e.quantity;
                } else if (45 < e.age < 60) {
                    between45To60 = e.quantity;
                } else {
                    over60 = e.quantity;
                }
            });
            aAge = [under18, between18To22, between23To27, between28To34, between35To44, between45To60, over60];
            if (chartAge) {
                chartAge.data.labels = ['<18', '18-22', '23-27', '28-34', '35-44', '45-60', '60+'];
                chartAge.data.datasets[0].data = aAge;
                chartAge.update();
            } else {
                chartAge = new Chart(age, {
                    type: 'bar',
                    data: {
                        labels: ['<18', '18-22', '23-27', '28-34', '35-44', '45-60', '60+'],
                        datasets: [{
                            data: aAge,
                            borderWidth: 1,
                            backgroundColor: [
                                'rgba(255, 99, 132, 0.5)',
                                'rgba(75, 192, 192, 0.5)',
                                'rgba(255, 205, 86, 0.5)',
                                'rgba(54, 162, 235, 0.5)'
                            ],
                            borderColor: 'rgba(75, 192, 192, 1)',
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        scales: {
                            x: {
                                beginAtZero: true,
                            },
                            y: {
                                display: false, // Ẩn thanh y
                                beginAtZero: true,
                            }
                        },
                        plugins: {
                            legend: {
                                display: false, // Ẩn chú thích
                            },
                        }
                    }
                });
            }
        })
    }

    $scope.analysicGender = function (listId) {
        $http.get(host + '/v1/monitor/gender', {
            params: { id: listId, duration: $scope.selectedTime }
        }).then(resp => {
            var male = 0;
            var female = 0;
            var nonBinary = 0;
            var notSpecific = 0;
            resp.data.data.forEach(e => {
                if (e.gender == 0) {
                    female = e.quantity;
                } else if (e.gender == 1) {
                    male = e.quantity;
                } else if (e.gender == 2) {
                    nonBinary = e.quantity;
                } else {
                    notSpecific = e.quantity;
                }
            })
            aGender = [female, male, nonBinary, notSpecific];
            if (chartGender) {
                chartGender.data.labels = ['Female', 'Male', 'Non-binary', 'Not specific'];
                chartGender.data.datasets[0].data = aGender;
                chartGender.update();
            } else {
                chartGender = new Chart(gender, {
                    type: 'doughnut',
                    data: {
                        //0         //1         //2           //3   
                        labels: ['Female', 'Male', 'Non-binary', 'Not specific'],
                        datasets: [{
                            data: aGender,
                            borderWidth: 1,
                            fill: true,
                            backgroundColor: [
                                'rgba(255, 99, 132, 0.5)',
                                'rgba(75, 192, 192, 0.5)',
                                'rgba(255, 205, 86, 0.5)',
                                'rgba(54, 162, 235, 0.5)'
                            ],
                            borderColor: 'rgba(75, 192, 192, 1)',
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                    }
                });
            }
        })
    }

    $scope.analysicCountry = function (listId) {
        anychart.onDocumentReady(function () {
            $http.get(host + '/v1/monitor/country', {
                params: { id: listId, duration: $scope.selectedTime }
            }).then(resp => {
                $scope.listCountries = resp.data.data;
                resp.data.data.forEach(e => {
                    var obj = { id: e.country, density: e.quantity }
                    aCountries.push(obj);
                });

                if (!mapChart) {
                    mapChart = anychart.map();
                    mapChart.geoData('anychart.maps.world');
                    mapChart.interactivity().selectionMode('none');
                    mapChart.padding(0);
                    var zoomController = anychart.ui.zoom();
                    zoomController.render(mapChart);
                    mapChart.container('world');
                } else {
                    dataSet = anychart.data.set(aCountries);
                    densityData = dataSet.mapAs({ value: 'density' });
                    series = mapChart.choropleth(densityData);
                }
                var dataSet = anychart.data.set(aCountries);
                var densityData = dataSet.mapAs({ value: 'density' });
                var series = mapChart.choropleth(densityData);

                var scale = anychart.scales.ordinalColor([
                    { less: 1000 },
                    { from: 1000, to: 10000 },
                    { from: 10000, to: 100000 },
                    { from: 100000, to: 500000 },
                    { from: 500000, to: 1000000 },
                    { from: 1000000, to: 10000000 },
                    { from: 10000000, to: 100000000 },
                    { from: 100000000, to: 500000000 },
                    { greater: 500000000 }
                ]);

                scale.colors([
                    '#81d4fa',
                    '#4fc3f7',
                    '#29b6f6',
                    '#039be5',
                    '#0288d1',
                    '#0277bd',
                    '#01579b',
                    '#014377',
                    '#000000'
                ]);

                var colorRange = mapChart.colorRange();
                colorRange.enabled(true).padding([0, 0, 20, 0]);
                colorRange
                    .ticks()
                    .enabled(true)
                    .stroke('3 #ffffff')
                    .position('center')
                    .length(7);
                colorRange.colorLineSize(5);
                colorRange.marker().size(7);
                colorRange
                    .labels()
                    .fontSize(11)
                    .padding(3, 0, 0, 0)
                    .format(function () {
                        var range = this.colorRange;
                        var name;
                        if (isFinite(range.start + range.end)) {
                            name = range.start + ' - ' + range.end;
                        } else if (isFinite(range.start)) {
                            name = 'More than ' + range.start;
                        } else {
                            name = 'Less than ' + range.end;
                        }
                        return name;
                    });

                series.colorScale(scale);
                try {
                    mapChart.draw();
                    var zoom = anychart.ui.zoom();
                    zoom.renderTo('zoom-controls');
                    zoom.target(mapChart);
                } catch (error) {
                    console.error(error);
                }
            })
        })
    }

    $scope.newListener = function () {
        const query = `{
            getNewListened(recordingId: ${$scope.recordingId}, duration: ${$scope.selectedTime}) {
                monitorid
                dateMonitor
              account {
                email
                username
                image{
                    url
                }
              }
            }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.listListener = data.getNewListened;
        })
    }

    $scope.statisticsRecord = function () {
        let url = host + "/v1/record-statistics";
        $http.get(url, {
            params: { duration: $scope.selectedTime },
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            var labels = [];
            var data = [];
            var colors = [];
            $scope.listRecord = resp.data.data;
            $scope.listRecord.forEach(item => {
                labels.push(item.recordingName);
                data.push(item.listened);
                var randomColor = 'rgba(' +
                    Math.floor(Math.random() * 256) + ',' +
                    Math.floor(Math.random() * 256) + ',' +
                    Math.floor(Math.random() * 256) + ',' +
                    '0.5)';
                colors.push(randomColor);
            })
            if (chartRecord) {
                chartRecord.data.labels = labels;
                chartRecord.data.datasets[0].data = data;
                chartRecord.data.datasets[0].backgroundColor = colors;
                chartRecord.update();
            } else {
                chartRecord = new Chart(record, {
                    type: 'bar',
                    data: {
                        labels: labels,
                        datasets: [{
                            data: data,
                            borderWidth: 1,
                            backgroundColor: colors,
                            borderColor: 'rgba(75, 192, 192, 1)',
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        scales: {
                            x: {
                                beginAtZero: true,
                            },
                            y: {
                                display: false, // Ẩn thanh y
                                beginAtZero: true,
                            }
                        },
                        plugins: {
                            legend: {
                                display: false, // Ẩn chú thích
                            },
                        }
                    }
                });
            }
        })
    }

    $scope.findAllFanAlsoLike = function (listRecordId) {
        let url = host + "/v1/monitor/fan-also-liked";
        $http.get(url, {
            params: { listRecord: listRecordId, duration: $scope.selectedTime }
        }).then(resp => {
            $scope.listListenerAlsoLiked = resp.data.data;
        })
    }

    $scope.changeTime = function () {
        if (Number($routeParams.id) > 0) {
            $scope.recordingId = $routeParams.id;
            $scope.Recording();
        } else {
            $scope.findAllFanAlsoLike($scope.listRecordId);
            $scope.analysicAge([...$scope.listRecordId]);
            $scope.analysicGender([...$scope.listRecordId]);
            $scope.analysicCountry([...$scope.listRecordId]);
            $scope.monitorFollow();
            $scope.statisticsRecord();
        }
        $scope.daysAgo = new Date(Date.now() - $scope.selectedTime * 24 * 60 * 60 * 1000);
    }

    if (Number($routeParams.id) > 0) {
        $scope.recordingId = $routeParams.id;
        $scope.Recording();
    } else {
        let url = host + '/v1/record-artist';
        $http.get(url, {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.listRecordId = [];
            resp.data.data.forEach(item => {
                $scope.listRecordId.push(item.recordingId)
                $scope.listened += item.listened
            })
            $scope.findAllFanAlsoLike($scope.listRecordId);
            $scope.analysicAge([...$scope.listRecordId]);
            $scope.analysicGender([...$scope.listRecordId]);
            $scope.analysicCountry([...$scope.listRecordId]);
            $scope.monitorFollow();
        }).catch(error => {
            console.log(error)
        })
        $scope.statisticsRecord();
    }
})