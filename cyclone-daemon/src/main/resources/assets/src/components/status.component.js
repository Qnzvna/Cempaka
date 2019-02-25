export const StatusComponent = {
    templateUrl: 'templates/status.html',
    controller: class StatusController
    {
        constructor($http, ngToast)
        {
            this.http = $http;
            this.ngToast = ngToast;
        }

        $onInit()
        {
            this.healthy = false;
            this.http({
                method: 'GET',
                url: '/api/status/node'
            }).then(response => {
                this.nodeStatus = response.data;
                this.healthy = true;
            }, error => {
                this.ngToast.danger(`Failed to get the status. ${error.data.message}`);
            })
        }
    }
};