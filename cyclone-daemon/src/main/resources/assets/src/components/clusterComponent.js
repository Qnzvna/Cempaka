export const ClusterComponent = {
    templateUrl: 'templates/cluster.html',
    controller: class ClusterController
    {
        constructor(clusterService)
        {
            this.clusterService = clusterService;
        }

        $onInit()
        {
            this.nodes = undefined;
            this.clusterService.getStatus().then(nodes => this.nodes = nodes);
        }
    }
};