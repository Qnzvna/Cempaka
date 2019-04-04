export const TestComponent = {
    bindings: {
        test: '<'
    },
    templateUrl: 'templates/test.html',
    controller: class TestController
    {
        constructor($routeParams, $http, ngToast)
        {
            this.routeParams = $routeParams;
            this.http = $http;
            this.ngToast = ngToast;
        }

        $onInit()
        {
            if (this.runConfiguration === undefined) {
                this.runConfiguration = {
                    parcelId: this.routeParams.parcelId,
                    testName: this.routeParams.testName,
                    loopCount: 1,
                    threadsNumber: 1,
                    parameters: {}
                };
            }
            if (this.parametersMetadata === undefined) {
                this.parametersMetadata = [];
            }
        }

        $onChanges(changes)
        {
            if (changes.test) {
                const test = changes.test.currentValue;
                if (test !== undefined) {
                    this.parametersMetadata = test.testMetadata.parameters;
                    this.runConfiguration = {
                        parcelId: test.parcelId,
                        testName: test.testMetadata.testName,
                        loopCount: 1,
                        threadsNumber: 1,
                        nodeIdentifiers: [],
                        parameters:
                            TestController.getParametersPlaceholders(test.testMetadata.parameters)
                    };
                } else {
                    this.http({
                        method: 'GET',
                        url: `/api/tests/parcel/${this.routeParams.parcelId}/name/${this.routeParams.testName}`
                    }).then(response => {
                        const parametersMetadata = response.data.parameters;
                        this.parametersMetadata = parametersMetadata;
                        this.runConfiguration.parameters =
                            TestController.getParametersPlaceholders(parametersMetadata);
                    }, error => {
                        this.ngToast.danger(`Failed to download metadata. ${error.data.message}`);
                    });
                }
            }
        }

        static getParametersPlaceholders(parametersMetadata)
        {
            let parameters = {};
            parametersMetadata.forEach(metadata => {
                parameters[metadata.name] = metadata.defaultValue;
            });
            return parameters;
        }

        runTest()
        {
            this.http({
                method: 'POST',
                url: '/api/tests/run',
                data: this.runConfiguration
            }).then(ignore => {
                this.ngToast.success('Test started successfully.');
            }, error => {
                this.ngToast.danger(`Failed to run test. ${error.data.message}`);
            })
        }
    }
};