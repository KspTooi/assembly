package com.ksptool.assembly.entity.ext;


public class QrCodeContainer<T> {

    // 二维码类型
    private String type;

    // 二维码内容
    private T content;

    /**
     * 构造函数
     *
     * @param type    类型
     * @param content 内容
     */
    public QrCodeContainer(String type, T content) {

        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("type不能为空");
        }

        if (content == null) {
            throw new IllegalArgumentException("content不能为空");
        }

        this.type = type;
        this.content = content;
    }

    /**
     * 创建QrCodeContainer
     *
     * @param type    类型
     * @param content 内容
     * @return QrCodeContainer
     */
    public static <T> QrCodeContainer<T> of(String type, T content) {
        return new QrCodeContainer<>(type, content);
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
