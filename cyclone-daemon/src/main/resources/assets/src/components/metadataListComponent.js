export const MetadataListComponent = {
    templateUrl: 'templates/metadataList.html',
    controller: class MetadataController
    {
        constructor($location, metadataService)
        {
            this.$location = $location;
            this.metadataService = metadataService;
        }

        $onInit()
        {
            this.metadata = undefined;
            this.loadMetadata();
        }

        loadMetadata()
        {
            this.metadataService.getAll().then(metadata => this.metadata = metadata);
        }

        deleteMetadata(metadata)
        {
            this.metadataService.delete(metadata.id).then(() => {
                this.metadata = _.without(this.metadata, metadata);
            });
        }

        addMetadata()
        {
            this.$location.path(`/metadata/add`);
        }
    }
};