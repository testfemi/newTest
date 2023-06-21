package interfaces;

public interface IDownloadable {

    void downloadFileType(String fileType);

    // String downloadFileType(String filType); // Alternative implementation option to consider

    boolean isSuccessfullyDownloaded();

    // boolean isSuccessfullyDownloaded(String fileName); // Alternative implementation option to consider
}
