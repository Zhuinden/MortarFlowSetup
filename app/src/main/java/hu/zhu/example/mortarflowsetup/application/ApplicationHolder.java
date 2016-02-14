package hu.zhu.example.mortarflowsetup.application;

public enum ApplicationHolder {
    INSTANCE;

    private CustomApplication customApplication;

    ApplicationHolder() {
    }

    void setApplication(CustomApplication customApplication) {
        this.customApplication = customApplication;
    }

    public CustomApplication getApplication() {
        return customApplication;
    }
}