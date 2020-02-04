export const TestExecutionsComponent = {
    templateUrl: 'templates/testExecutions.html',
    controller: class TestExecutionsController
    {
        constructor($interval, $location, testService, executionService, ngToast)
        {
            this.$interval = $interval;
            this.$location = $location;
            this.testService = testService;
            this.executionService = executionService;
            this.ngToast = ngToast;
        }

        $onInit()
        {
            this.executions = undefined;
            this.offset = 0;
            this.firstPage = true;
            this.lastPage = true;
            this.loadExecutions();
            this.startRefreshingRunningTests();
        }

        loadExecutions()
        {
            this.testService.getTestsExecutions(this.getLimit(), this.offset)
               .then(executionsPage => {
                   this.executions = this.mapExecutions(executionsPage.testExecutions);
                   this.lastPage = !executionsPage.hasNext;
                   this.firstPage = this.offset === 0;
               });
        }

        startRefreshingRunningTests()
        {
            let loadingExecutions = [];
            this.$interval(() => {
                if (!_.isUndefined(this.executions) && _.isEmpty(loadingExecutions)) {
                    _.keys(this.executions)
                        .filter(id => this.isTestRunning(id))
                        .forEach(id => this.reloadTestExecution(id)
                            .then(() => _.pull(loadingExecutions, id)));
                }
            }, 5000);
        }

        reloadTestExecution(id)
        {
            return this.testService.getTestExecution(id)
                .then(executions => this.executions[id] = this.mapNodesExecutions(executions));
        }

        mapExecutions(executions)
        {
            return _.mapValues(_.groupBy(executions, execution => execution.id),
                executions => this.mapNodesExecutions(executions));
        }

        mapNodesExecutions(executions)
        {
            const executionsByNode = _.groupBy(executions, execution => execution.node);
            return {
                names: this.executionService.extractTestNames(executionsByNode),
                states: this.executionService.extractStates(executionsByNode)
            };
        }

        deleteExecution(id)
        {
            this.testService.deleteExecution(id)
                .then(() => this.executions = _.omit(this.executions, [id]));
        }

        stopExecution(id)
        {
            this.testService.stopTest(id).then(() => this.reloadTestExecution(id));
        }

        showExecution(id)
        {
            this.$location.path(`/tests/executions/${id}`);
        }

        isTestRunning(id)
        {
            return this.executionService.isTestRunning(_.values(this.executions[id].states));
        }

        areExecutionsEmpty()
        {
            return !_.isUndefined(this.executions) && _.isEmpty(this.executions);
        }

        nextPage()
        {
            this.offset += this.getLimit();
            this.loadExecutions();
        }

        previousPage()
        {
            this.offset -= this.getLimit();
            this.loadExecutions();
        }

        getLimit()
        {
            return 50;
        }
    }
};