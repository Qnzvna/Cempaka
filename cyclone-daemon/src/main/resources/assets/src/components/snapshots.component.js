export const SnapshotsComponent = {
    templateUrl: 'templates/snapshots.html',
    controller: class SnapshotsController
    {
        constructor($http, ngToast)
        {
            this.http = $http;
            this.ngToast = ngToast;
        }

        $onInit()
        {
            this.metadata = [];
            this.snapshots = {};
            this.snapshotId = undefined;
            this.http({
                method: 'GET',
                url: '/api/tests/snapshots'
            }).then(response => {
                this.metadata = response.data;
            }, error => {
                this.ngToast.danger(`Failed to get snapshots metadata. ${error.data.message}`);
            });
            setInterval(() => this.getSnapshots(this.snapshotId), 10000);
        }

        showSnapshot(id)
        {
            this.snapshotId = id;
            this.getSnapshots(id);
        }

        getSnapshots(id)
        {
            this.http({
                method: 'GET',
                url: `/api/tests/snapshots/${id}`
            }).then(response => {
                this.snapshots = response.data;
            }, error => {
                this.ngToast.danger(`Failed to get snapshot. ${error.data.message}`);
            });
        }

        isActive(id)
        {
            if (this.snapshots.length > 0) {
                return this.snapshots[0].testId === id;
            } else {
                return false;
            }
        }

        getStatusStyle(status)
        {
            switch (status) {
                case 'INITIALIZED':
                    return 'badge-secondary';
                case 'STARTED':
                    return 'badge-primary';
                case 'ENDED':
                    return 'badge-success';
                case 'FAILED':
                    return 'badge-danger';
            }
            return 'badge-light';
        }
    }
};