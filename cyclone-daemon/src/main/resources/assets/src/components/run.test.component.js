export const RunTestComponent = {
    templateUrl: 'templates/run.test.html',
    controller: class RunTestController
    {
        constructor($http, ngToast)
        {
            this.http = $http;
            this.ngToast = ngToast;
        }

        $onInit()
        {
            this.http({
                method: 'GET',
                url: '/api/tests'
            }).then(response => {
                this.tests = response.data.flatMap(metadata => {
                    return metadata.testsMetadata.map(testMetadata => {
                        return {
                            parcelId: metadata.id,
                            testMetadata: testMetadata
                        }
                    })
                });
                this.test = this.tests[0] || undefined;
            }, error => {
                this.ngToast.danger(`Failed to download metadata. ${error.data.message}`);
            })
        }
    }
};