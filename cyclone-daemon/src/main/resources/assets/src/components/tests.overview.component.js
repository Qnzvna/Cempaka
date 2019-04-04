export const TestsOverviewComponent = {
    templateUrl: 'templates/tests.overview.html',
    controller: class TestsHistoryController
    {
        constructor($http, ngToast)
        {
            this.http = $http;
            this.ngToast = ngToast;
        }

        $onInit()
        {
            this.state = 'ENDED';
            this.tests = [];
            this.test = undefined;
            this.metrics = undefined;

            this.loadConfigurations();
        }

        setTest(test)
        {
            this.test = test;
            this.loadMetrics(test);
        }

        loadMetrics(test)
        {
            this.http({
                method: 'GET',
                url: `/api/tests/${test.id}/metrics`
            }).then(response => {
                this.metrics = response.data;
            }, error => {
                this.ngToast.danger(`Failed to get metrics. ${error.data.message}`);
            });
        }

        loadConfigurations()
        {
            this.tests = [];
            this.http({
                method: 'GET',
                url: `/api/tests/states/${this.state}`
            }).then(response => {
                response.data.forEach(id => {
                    this.http({
                        method: 'GET',
                        url: `/api/tests/${id}/configuration`
                    }).then(response => {
                        this.tests.push({
                            id: id,
                            configuration: response.data
                        });
                    }, error => {
                        this.ngToast.danger(`Failed to get configuration. ${error.data.message}`);
                    });
                });
            }, error => {
                this.ngToast.danger(`Failed to get tests. ${error.data.message}`);
            });
        }

        setState(state)
        {
            this.state = state;
            this.loadConfigurations();
        }

        isActiveState(state)
        {
            return this.state === state;
        }
    }
};