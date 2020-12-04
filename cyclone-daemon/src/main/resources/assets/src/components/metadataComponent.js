export const MetadataComponent = {
    templateUrl: 'templates/metadata.html',
    controller: class MetadataController
    {
        constructor($routeParams, ngToast, Upload, metadataService)
        {
            this.$routeParams = $routeParams;
            this.ngToast = ngToast;
            this.Upload = Upload;
            this.metadataService = metadataService;
        }

        $onInit()
        {
            this.id = this.$routeParams.id;
            this.metadata = {
                id: '',
                value: ''
            };
        }
    }
}