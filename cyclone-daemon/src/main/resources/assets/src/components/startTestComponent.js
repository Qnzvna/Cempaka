export const StartTestComponent = {
    templateUrl: 'templates/startTest.html',
    controller: class RunTestController
    {
        constructor(testService, clusterService)
        {
            this.testService = testService;
            this.clusterService = clusterService;
        }

        $onInit()
        {
            this.aliveNodes = [];
            this.properties = {
                loopCount: 1,
                duration: 0,
                threadsNumber: 1,
                parameters: {},
                nodes: [],
                jvmOptions: ''
            };
            this.testService.getTests()
                .then(tests => {
                    this.tests = tests;
                    this.test = tests[0] || undefined;
                    if (!_.isUndefined(this.test)) {
                        this.reduceParameters(this.test.parameters);
                    }
                });
            this.clusterService.getStatus().then(nodes => {
                this.aliveNodes = _.invertBy(nodes)[true];
                this.properties.nodes = [this.aliveNodes[0]];
            });
        }

        startTest()
        {
            this.testService.startTest({
                parcelId: this.test.parcelId,
                testName: this.test.name,
                loopCount: this.properties.duration == 0 ? this.properties.loopCount : null,
                duration: this.properties.duration != 0 ? "PT" + this.properties.duration + "M" : null,
                threadsNumber: this.properties.threadsNumber,
                parameters: this.properties.parameters,
                nodes: this.properties.nodes,
                jvmOptions: this.properties.jvmOptions
            });
        }

        reduceParameters(parameters)
        {
            this.properties.parameters = parameters.reduce((map, parameter) => {
                map[parameter.name] = parameter.defaultValue;
                return map;
            }, {});
        }

        isNodeSelected(node)
        {
            return this.properties.nodes.includes(node);
        }
    }
};
