export const ParcelComponent = {
    templateUrl: "templates/parcel.html",
    controller: class ParcelController
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
            }, error => {
                this.ngToast.danger(`Parcel failed to upload. ${error.data.message}`);
                this.uploading = false;
            });
        }
    }
};