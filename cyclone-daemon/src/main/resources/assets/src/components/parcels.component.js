export const ParcelsComponent = {
    templateUrl: 'templates/parcels.html',
    controller: class ParcelsController {
        constructor($http, ngToast) {
            this.http = $http;
            this.ngToast = ngToast;
        }

        $onInit() {
            this.parcels = [];
            this.loadParcels();
        }

        loadParcels() {
            this.http({
                'method': 'GET',
                'url': '/api/tests'
            }).then((response) => {
                this.parcels = response.data;
            }, (error) => {
                this.ngToast.danger(`Failed to get the parcels. ${error.data.message}`);
            });
        }

        deleteParcel(id) {
            this.http({
                'method': 'DELETE',
                'url': `/api/parcels/${id}`
            }).then((response) => {
                this.loadParcels();
                this.ngToast.success('Parcel deleted successfully.');
            }, (error) => {
                this.ngToast.danger(`Failed to delete parcel. ${error.data.message}`);
            })
        }
    }
};