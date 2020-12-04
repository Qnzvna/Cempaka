export const MetadataForm = {
    templateUrl: 'templates/metadataForm.html',
    bindings: {
        metadata: '<',
    },
    controller: class MetadataFormController
    {
        constructor($location, ngToast, Upload, metadataService)
        {
            this.$location = $location;
            this.ngToast = ngToast;
            this.Upload = Upload;
            this.metadataService = metadataService;
        }

        $onInit()
        {
            this.metadataFile = undefined;
            this.uploading = false;
            this.inputType = 'File';
        }

        saveMetadata()
        {
            this.uploading = true;
            switch (this.inputType) {
                case 'File':
                    this.Upload.upload({
                        url: `/api/metadata/${this.metadata.id}`,
                        data: {file: this.metadataFile}
                    }).then(ignore => {
                        this.ngToast.success('Metadata uploaded successfully.');
                        this.uploading = false;
                        this.uploaded = 0;
                        this.$location.path(`/metadata`);
                    }, error => {
                        this.ngToast.danger(`Metadata failed to upload. ${error.data.message}`);
                        this.uploading = false;
                        this.uploaded = 0;
                    }, event => {
                        this.uploaded = parseInt(100.0 * event.loaded / event.total);
                    });
                    break;
                case 'Text':
                    this.metadataService.put(this.metadata).then(() => {
                        this.ngToast.success('Metadata uploaded successfully.');
                        this.$location.path(`/metadata`);
                    }, () => {
                        this.uploading = false
                    });
                    break;
            }

        }
    }
}