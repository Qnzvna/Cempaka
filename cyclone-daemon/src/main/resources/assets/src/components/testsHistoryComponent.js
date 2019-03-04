export const TestsHistoryComponent = {
    templateUrl: 'templates/tests.history.html',
    controller: class TestsHistoryController
    {
        constructor($http, ngToast)
        {
            this.http = $http;
            this.ngToast = ngToast;
        }

        $onInit()
        {
            this.metadata = [];
            this.testRunId = undefined;
            this.events = [];
            this.metrics = [];
            this.http({
                method: 'GET',
                url: '/api/tests/metadata'
            }).then(response => {
                this.metadata = response.data;
            }, error => {
                this.ngToast.danger(`Failed to get metadata. ${error.data.message}`);
            });
        }

        selectTest(id)
        {
            this.testRunId = id;
            this.getTestEvents(id);
            this.getMetrics(id);
        }

        getTestEvents(id)
        {
            this.http({
                method: 'GET',
                url: `/api/tests/events/${id}`
            }).then(response => {
                this.events = response.data;
            }, error => {
                this.ngToast.danger(`Failed to get events. ${error.data.message}`);
            });
        }

        getMetrics(id)
        {
            this.http({
                method: 'GET',
                url: `/api/tests/metrics/${id}`
            }).then(response => {
                this.metrics = response.data;
            }, error => {
                this.ngToast.danger(`Failed to get metrics. ${error.data.message}`);
            });
        }

        isActive(id)
        {
            return this.testRunId === id;
        }

        getStatusStyle(status)
        {
            switch (status) {
                case 'INITIALIZED':
                    return 'badge-secondary';
                case 'STARTED':
                    return 'badge-primary';
                case 'ENDED':
                    return 'badge-success';
                case 'FAILED':
                    return 'badge-danger';
            }
            return 'badge-light';
        }
    }
};