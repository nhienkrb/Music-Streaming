var host = "http://localhost:8080/api";
app.controller('analysisCtrl', function ($scope, $http, graphqlService, $routeParams, $filter) {
    $('#myTab a').click(function (e) {
        e.preventDefault()
        $(this).tab('show')
    })
    $scope.podcast = JSON.parse(localStorage.getItem('podcast'));
    $scope.selectedTime = 7;
    $scope.currentDate = $filter('date')(new Date(), 'yyyy-MM-dd');
    $scope.daysAgo = new Date(Date.now() - $scope.selectedTime * 24 * 60 * 60 * 1000);
    $scope.listCountries = [];
    $scope.listEpisode = [];
    $scope.listListener = [];
    var aCountries = [];
    var aAge;
    var aGender = [];
    $scope.Episode = function () {
        const query = `{
                episodeById(episodeId: ${$scope.episodeId}) {
                    episodeId
                    episodeTitle
                    listened
                    publishDate
                    monitorEp{
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
            $scope.monitor = data.episodeById.monitors;
            $scope.listened = data.episodeById.listened;
            $scope.analysicAge([$scope.episodeId]);
            $scope.analysicGender([$scope.episodeId]);
            $scope.analysicCountry([$scope.episodeId]);
            $scope.newListener();
        }).catch(err => {

        })
    }


    const ctx = $('#chartFollower');
    const age = $('#age');
    const episode = $('#episode');
    const rating = $('#rating');
    const podcastListened = $('#podcastListened');
    const gender = $('#gender');
    var chartFollow;
    var chartPodcast;
    var chartAge;
    var chartEpisode;
    var chartGender;
    var mapChart;

    $scope.monitorFollow = function () {
        $http.get(host + "/v1/account", {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            let url = host + "/v1/monitor-follow";
            $http.get(url, {
                params: { email: resp.data.data.email, role: 3, duration: $scope.selectedTime }
            }).then(respChart => {
                if (respChart.data.data != null) {
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
                }
            })
        }).catch(error => {
            console.log("Not found artist profile")
        })
    }

    $scope.analysicAge = function (listId) {
        $http.get(host + '/v1/monitor-episode/age', {
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
        $http.get(host + '/v1/monitor-episode/gender', {
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
            $http.get(host + '/v1/monitor-episode/country', {
                params: { id: listId, duration: $scope.selectedTime }
            }).then(resp => {
                var map = anychart.map();
                map.geoData('anychart.maps.world');
                map.interactivity().selectionMode('none');
                map.padding(0);
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
            getNewListenedEpisode(episodeId: ${$scope.episodeId}, duration: ${$scope.selectedTime}) {
                id
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
            $scope.listListener = data.getNewListenedEpisode;
        })
    }

    $scope.statisticsEpisode = function (list) {
        var labels = [];
        var data = [];
        var colors = [];
        list.forEach(item => {
            if (new Date(item.publishDate) >= new Date(Date.now() - $scope.selectedTime * 24 * 60 * 60 * 1000)) {
                labels.push(item.episodeTitle);
                data.push(item.listened);
                var randomColor = 'rgba(' +
                    Math.floor(Math.random() * 256) + ',' +
                    Math.floor(Math.random() * 256) + ',' +
                    Math.floor(Math.random() * 256) + ',' +
                    '0.5)';
                colors.push(randomColor);
            }
            $scope.listened += item.listened;
        })
        if (chartEpisode) {
            chartEpisode.data.labels = labels;
            chartEpisode.data.datasets[0].data = data;
            chartEpisode.data.datasets[0].backgroundColor = colors;
            chartEpisode.update();
        } else {
            chartEpisode = new Chart(episode, {
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
                            beforeUpdate(axis) {
                                const labels = axis.chart.data.labels;
                                for (let i = 0; i < labels.length; i++) {
                                    const lbl = labels[i];
                                    if (typeof lbl === 'string' && lbl.length > 30) {
                                        labels[i] = lbl.substring(0, 30); // cutting
                                    }
                                }
                            }
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
    }

    $scope.chartAll = function () {
        $scope.listAllPodcast().then(data => {
            var labels = [];
            var rate = [];
            var listEpId = [];
            var listEp = [];
            var listListenPodcast = [];
            data.findPodcastByEmail.forEach(item => {
                var listen = 0;
                if (new Date(item.releaseDate) >= new Date(Date.now() - $scope.selectedTime * 24 * 60 * 60 * 1000)) {
                    labels.push(item.podcastName);
                    rate.push(item.rate);

                }
                item.Episodes.forEach(item => {
                    listEpId.push(item.episodeId);
                    listEp.push(item);
                    listen += item.listened;
                })
                listListenPodcast.push(listen);
            })
            $scope.findAllFanAlsoLike(listEpId);
            $scope.analysicAge([...listEpId]);
            $scope.analysicGender([...listEpId]);
            $scope.analysicCountry([...listEpId]);
            $scope.statisticsEpisode(listEp);

            // new Chart(rating, {
            //     type: 'bar',
            //     data: {
            //         labels: labels,
            //         datasets: [{
            //             data: rate,
            //             borderWidth: 1,
            //             backgroundColor: [
            //                 'rgba(255, 99, 132, 0.5)',
            //                 'rgba(75, 192, 192, 0.5)',
            //                 'rgba(255, 205, 86, 0.5)',
            //                 'rgba(54, 162, 235, 0.5)'
            //             ],
            //             borderWidth: 1
            //         }]
            //     },
            //     options: {
            //         responsive: true,
            //         maintainAspectRatio: false,
            //         aspectRatio: 1,
            //         scales: {
            //             x: {
            //                 beginAtZero: true,
            //             },
            //             y: {
            //                 display: false, // Ẩn thanh y
            //                 beginAtZero: true,
            //             }
            //         },
            //         plugins: {
            //             legend: {
            //                 display: false, // Ẩn chú thích
            //             },
            //         },
            //         height: 300,
            //     }
            // });
            if (chartPodcast) {
                chartPodcast.data.labels = labels;
                chartPodcast.data.datasets[0].data = listListenPodcast;
                chartPodcast.update();
            } else {
                chartPodcast = new Chart(podcastListened, {
                    type: 'bar',
                    data: {
                        labels: labels,
                        datasets: [{
                            data: listListenPodcast,
                            borderWidth: 1,
                            backgroundColor: [
                                'rgba(255, 99, 132, 0.5)',
                                'rgba(75, 192, 192, 0.5)',
                                'rgba(255, 205, 86, 0.5)',
                                'rgba(54, 162, 235, 0.5)'
                            ],
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        aspectRatio: 1,
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
                        },
                        height: 300,
                    }
                });
            }
        })
    }

    $scope.findAllFanAlsoLike = function (listEpisodeId) {
        let url = host + "/v1/monitor-episode/fan-also-liked";
        $http.get(url, {
            params: { listEpisode: listEpisodeId, duration: $scope.selectedTime }
        }).then(resp => {
            $scope.listListenerAlsoLiked = resp.data.data;
        })
    }

    //Char Map
    $scope.findAllEpisode = function () {
        return new Promise((resolve, reject) => {
            let url = host + '/v1/podcast-episode/' + $scope.podcast.podcastId;
            $http.get(url, {
                headers: { 'Authorization': 'Bearer ' + getCookie('token') }
            }).then(data => resolve(data))
                .catch(error => reject(error));
        })
    }

    $scope.listAllPodcast = function () {
        return new Promise((resolve, reject) => {
            const query = `{
                findPodcastByEmail(email: "${$scope.podcast.account.email}") {
                    podcastId
                    podcastName
                    rate
                    releaseDate
                    Episodes {
                        episodeId
                        episodeTitle
                        listened
                        publishDate
                    }
                }
            }`
            graphqlService.executeQuery(query)
                .then(data => resolve(data))
                .catch(error => reject(error));
        });
    }

    if (Number($routeParams.id) > 0) {
        $scope.listened = 0;
        $scope.episodeId = $routeParams.id;
        $scope.Episode();
    } else {
        $scope.listened = 0;
        $scope.chartAll();
        $scope.monitorFollow();
    }

    $scope.type='all'
    $scope.select = function (type) {
        $scope.type=type
        $scope.listened = 0;
        if ($scope.type === 'all') {
            $scope.chartAll();
        } else {
            $scope.findAllEpisode().then(resp => {
                $scope.listId = [];
                resp.data.data.forEach(item => {
                    listId.push(item.episodeId)
                })
                $scope.findAllFanAlsoLike($scope.listId);
                $scope.analysicAge([...$scope.listId]);
                $scope.analysicGender([...$scope.listId]);
                $scope.analysicCountry([...$scope.listId]);
                $scope.statisticsEpisode(resp.data.data);
            }).catch(error => {
                console.log(error)
            })
        }
        $scope.monitorFollow();
    }

    $scope.changeTime = function () {
        $scope.listened = 0;
        if (Number($routeParams.id) > 0) {       
            $scope.Episode();
        } else {
            if ($scope.type === 'all') {
                $scope.chartAll();
            } else {
                $scope.findAllEpisode().then(resp => {
                    $scope.listId = [];
                    resp.data.data.forEach(item => {
                        $scope.listId.push(item.episodeId)
                    })
                    $scope.findAllFanAlsoLike($scope.listId);
                    $scope.analysicAge([...$scope.listId]);
                    $scope.analysicGender([...$scope.listId]);
                    $scope.analysicCountry([...$scope.listId]);
                    $scope.statisticsEpisode(resp.data.data);
                }).catch(error => {
                    console.log(error)
                })
            }
            $scope.monitorFollow();
        } 
        $scope.daysAgo = new Date(Date.now() - $scope.selectedTime * 24 * 60 * 60 * 1000);
    }
})