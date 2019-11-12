export class ExecutionService
{
    getStateBadgeType(state)
    {
        switch (state) {
            case 'INITIALIZED':
                return 'info';
            case 'STARTED':
                return 'primary';
            case 'ENDED':
                return 'success';
            case 'ABORTED':
                return 'dark';
            case 'ERROR':
                return 'danger';
        }
    }

    isTestRunning(states)
    {
        return _.includes(states, 'INITIALIZED') || _.includes(states, 'STARTED');
    }

    groupByNode(executions)
    {
        return _.groupBy(executions, execution => execution.node);
    }

    extractStates(executionsByNode)
    {
        return _.mapValues(executionsByNode, executions => executions[0].state);
    }

    extractTestNames(executionsByNode)
    {
        return _.mapValues(executionsByNode,
            executions => executions[0].properties.testName);
    }
}