export class MetadataService
{
    constructor($http, ngToast)
    {
        this.$http = $http;
        this.ngToast = ngToast;
    }

    getAll()
    {
        return this.$http({
            method: 'GET',
            url: '/api/metadata'
        }).then(response => response.data,
            error => {
                this.ngToast.danger(`Failed to get metadata. ${error.data.message}`);
                throw error;
            });
    }

    delete(id)
    {
        return this.$http({
            method: 'DELETE',
            url: `/api/metadata/${id}`
        }).then(response => response.data,
            error => {
                this.ngToast.danger(`Failed to delete metadata. ${error.data.message}`);
                throw error;
            });
    }

    get(id)
    {
        return this.$http({
            method: 'GET',
            url: `/api/metadata/${id}`
        }).then(response => response.data);
    }

    put(metadata)
    {
        return this.$http({
            method: 'POST',
            url: `/api/metadata/${metadata.id}`,
            data: metadata.value
        }).then(response => response.data,
            error => {
                this.ngToast.danger(`Failed to upload metadata. ${error.data.message}`);
                throw error;
            });
    }
}
