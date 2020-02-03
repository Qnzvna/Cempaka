export class TestsService
{
    constructor($http, ngToast)
    {
        this.$http = $http;
        this.ngToast = ngToast;
    }

    getTests()
    {
        return this.$http({
            method: 'GET',
            url: '/api/tests'
        }).then(response => response.data,
            error => {
                this.ngToast.danger(`Failed to download tests. ${error.data.message}`);
                throw error;
            });
    }

    startTest(executionProperties)
    {
        return this.$http({
            method: 'POST',
            url: '/api/tests/start',
            data: executionProperties
        }).then(response => this.ngToast.success(`Test started successfully: ${response.data}`),
            error => {
                this.ngToast.danger(`Failed to run test. ${error.data.message}`);
                throw error;
            });
    }

    stopTest(id)
    {
        return this.$http({
            method: 'post',
            url: `/api/tests/${id}/stop`
        }).then(() => this.ngToast.success('Test stopped.'),
            error => {
                this.ngToast.danger(`Failed to stop test. ${error.data.message}`);
                throw error;
            });
    }

    deleteTest(id)
    {
        return this.$http({
            'method': 'DELETE',
            'url': `/api/parcels/${id}`
        }).then(() => this.ngToast.success('Test deleted successfully.'),
            error => this.ngToast.danger(`Failed to delete parcel. ${error.data.message}`))
    }

    getTestsExecutions(limit, offset)
    {
        return this.$http({
            method: 'GET',
            url: `/api/tests/executions?limit=${limit}&offset=${offset}`
        }).then(response => response.data,
            error => {
                this.ngToast.danger(`Failed to get test executions. ${error.data.message}`);
                throw error;
            });
    }

    getTestExecution(id)
    {
        return this.$http({
            method: 'GET',
            url: `/api/tests/executions/${id}`
        }).then(response => response.data,
            error => {
                this.ngToast.danger(
                    `Failed to get test execution by id [${id}]. ${error.data.message}`);
                throw error;
            });
    }

    deleteExecution(id)
    {
        return this.$http({
            method: 'DELETE',
            url: `/api/tests/executions/${id}`
        }).then(() => this.ngToast.success('Test execution deleted.'),
            error => {
                this.ngToast.danger(`Failed to delete test execution. ${error.data.message}`);
                throw error;
            });
    }

    getTestMetrics(id)
    {
        return this.$http({
            method: 'GET',
            url: `/api/tests/executions/${id}/metrics`
        }).then(response => response.data,
            error => {
                this.ngToast.danger(
                    `Failed to get test metrics by id [${id}]. ${error.data.message}`);
                throw error;
            });
    }
}