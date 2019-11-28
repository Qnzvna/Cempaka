export const TestExecutionComponent = {
    templateUrl: 'templates/testExecution.html',
    controller: class TestExecutionController
    {
        constructor($routeParams, $interval, testService, executionService)
        {
            this.$routeParams = $routeParams;
            this.$interval = $interval;
            this.testsService = testService;
            this.executionService = executionService;
        }

        $onInit()
        {
            this.id = this.$routeParams.id;
            this.executions = undefined;
            this.metrics = undefined;
            this.reloadExecution();
            this.reloadMetrics();
            this.$interval(() => {
            });
            this.$interval(() => {
                if (this.isTestRunning()) {
                    this.reloadExecution();
                    this.reloadMetrics();
                }
            }, 10000);
        }

        reloadExecution()
        {
            this.testsService.getTestExecution(this.id).then(executions => {
                this.executions = executions;
                this.states = this.executionService.extractStates(
                    this.executionService.groupByNode(executions));
            });
        }

        reloadMetrics()
        {
            this.testsService.getTestMetrics(this.id).then(metrics => {
                const size = _.uniq(_.map(metrics, metric => metric.name)).length;
                this.metrics = _.slice(metrics, 0, size);
                this.metrics = _.sortBy(this.metrics, metric => metric.name)
            });
        }

        formatTimestamp(timestamp)
        {
            return moment.unix(timestamp).format();
        }

        isTestRunning()
        {
            return this.executionService.isTestRunning(_.values(this.states));
        }

        filterProperties(properties)
        {
            return _.omit(properties, ['parameters', 'nodes']);
        }
    }
};
