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
                threadsNumber: 1,
                parameters: {},
                nodes: [],
                jvmOptions: ''
            };
            this.testService.getTests()
                .then(tests => {
                    this.tests = tests;
                    this.test = tests[0] || undefined;
                    this.changeParameters();
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
                loopCount: this.properties.loopCount,
                threadsNumber: this.properties.threadsNumber,
                parameters: this.properties.parameters,
                nodes: this.properties.nodes,
                jvmOptions: this.properties.jvmOptions
            });
        }

        changeParameters()
        {
            this.properties.parameters = this.test.parameters.reduce((map, parameter) => {
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