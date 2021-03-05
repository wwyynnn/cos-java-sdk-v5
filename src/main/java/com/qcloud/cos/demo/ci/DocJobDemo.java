package com.qcloud.cos.demo.ci;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ciModel.common.MediaOutputObject;
import com.qcloud.cos.model.ciModel.job.*;

/**
 * 文档预览任务相关demo
 */
public class DocJobDemo {
    public static void main(String[] args) {
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSClient client = ClientUtils.getTestClient();
        // 2 调用要使用的方法。
        createDocJobs(client);
    }

    /**
     * createMediaJobs 接口用于创建任务。
     *
     * @param client
     */
    public static void createDocJobs(COSClient client) {
        //1.创建任务请求对象
        DocJobRequest request = new DocJobRequest();
        //2.添加请求参数 参数详情请见api接口文档

        // Request 中的具体数据：
        request.setBucketName("examplebucket-1250000000"); // Bucket 的命名规则为 BucketName-APPID，

        DocJobObject docJobObject = request.getDocJobObject();
        docJobObject.setTag("DocProcess"); // 文档预览固定传 DocProcess
        docJobObject.getInput().setObject("demo.docx"); // 文件路径
        docJobObject.setQueueId("pc02270c617ae4b6d9b0a52cb1c*****"); // 任务所在的队列ID

        // DocProcess 的具体数据：（all not requested)
        DocProcessObject docProcessObject = docJobObject.getOperation().getDocProcessObject();
        docProcessObject.setQuality("100"); // 生成预览图的图片质量，取值范围 [1-100]，默认值100。
        docProcessObject.setZoom("100"); // 预览图片的缩放参数，取值范围[10-200]， 默认值100。
        docProcessObject.setStartPage("1"); // 从第 X 页开始转换
        docProcessObject.setEndPage("3"); // 转换至第 X 页
        docProcessObject.setTgtType("png"); // 转换输出目标文件类型
        docProcessObject.setDocPassword("123"); // Office 文档的打开密码

        //  Output 的具体数据：
        MediaOutputObject output = docJobObject.getOperation().getOutput();
        output.setRegion("ap-chongqing"); // 存储桶的地域
        output.setBucket("examplebucket-1250000000"); // 存储结果的存储桶
        output.setObject("mark/pic-${Page}.jpg"); // 输出文件路径。

        //3.调用接口,获取任务响应对象
        DocJobResponse docProcessJobs = client.createDocProcessJobs(request);
        System.out.println(docProcessJobs);
    }

    /**
     * describeMediaJob 根据jobId查询任务信息
     *
     * @param client
     */
    public static void describeMediaJob(COSClient client) {
        //1.创建任务请求对象
        DocJobRequest request = new DocJobRequest();
        //2.添加请求参数 参数详情请见api接口文档
        request.setBucketName("examplebucket-1250000000");
        request.setJobId("d75b6ea083df711eb8d09476dfb8*****");
        //3.调用接口,获取任务响应对象
        DocJobResponse docJobResponse = client.describeDocProcessJob(request);
        System.out.println(docJobResponse);
    }

    /**
     * describeMediaJobs 查询任务列表
     *
     * @param client
     */
    public static void describeMediaJobs(COSClient client) {
        //1.创建任务请求对象
        DocJobListRequest request = new DocJobListRequest();
        //2.添加请求参数 参数详情请见api接口文档
        request.setBucketName("examplebucket-1250000000");
        request.setQueueId("pc02270c617ae4b6d9b0a52cb1c*****");
        request.setTag("DocProcess");
        request.setStartCreationTime("2020-12-10T16:20:07+0800");
        //3.调用接口,获取任务响应对象
        DocJobListResponse docJobResponse = client.describeDocProcessJobs(request);
        for (DocJobDetail jobDetail : docJobResponse.getDocJobDetailList()) {
            System.out.println(jobDetail);
        }
    }
}
