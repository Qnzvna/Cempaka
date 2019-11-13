export const UploadParcelComponent = {
    templateUrl: 'templates/uploadParcel.html',
    bindings: {
        onUploaded: '&'
    },
    controller: class UploadParcelController
    {
        constructor(Upload, ngToast)
        {
            this.Upload = Upload;
            this.ngToast = ngToast;
        }

        $onInit()
        {
            this.uploading = false;
            this.parcel = {
                id: undefined
            };
            this.uploaded = 0;
        }

        uploadParcel(file)
        {
            this.uploading = true;
            this.Upload.upload({
                url: '/api/parcels',
                data: {file: file}
            }).then(ignore => {
                this.ngToast.success('Parcel uploaded successfully');
                this.uploading = false;
                this.uploaded = 0;
            }, error => {
                this.ngToast.danger(`Parcel failed to upload. ${error.data.message}`);
                this.uploading = false;
                this.uploaded = 0;
            }, event => {
                this.uploaded = parseInt(100.0 * event.loaded / event.total);
            });
        }
    }
};