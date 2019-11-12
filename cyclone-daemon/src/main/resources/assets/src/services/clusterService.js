export class ClusterService
{
    constructor($http, ngToast)
    {
        this.$http = $http;
        this.ngToast = ngToast;
    }

    getStatus()
    {
        return this.$http({
            method: 'GET',
            url: '/api/cluster/status'
        }).then(response => response.data,
            error => this.ngToast.danger(`Failed to get the status. ${error.data.message}`));
    }
}