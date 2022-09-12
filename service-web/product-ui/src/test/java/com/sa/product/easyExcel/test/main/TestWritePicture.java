package com.sa.product.easyExcel.test.main;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.ImageData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.util.ListUtils;
import com.sa.product.easyExcel.test.dto.ImageDTO;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TestWritePicture {
    @Test
    public void testOnceWrite() {
        String fileName = "test_write_image.xlsx";
        String imagePath = "image.jpg";
        try (InputStream inputStream = FileUtils.openInputStream(new File(imagePath))) {
            List<ImageDTO> list = ListUtils.newArrayList();
            ImageDTO imagedto = new ImageDTO();
            list.add(imagedto);
            // 可以放入5种类型的文件
            imagedto.setByteArray(FileUtils.readFileToByteArray(new File(imagePath)));
            imagedto.setFile(new File(imagePath));
            //DTO 中使用注解的方式进行转换
            imagedto.setString(imagePath);
            imagedto.setInputStream(inputStream);
//            imagedto.setUrl(new URL(
//                    "https://raw.githubusercontent.com/alibaba/easyexcel/master/src/test/resources/converter/img.jpg"));
            //一个单元格放多张图片
            WriteCellData<Void> writeCellData = new WriteCellData<>();
            imagedto.setWriteCellDataFile(writeCellData);
            // 这里可以设置为 EMPTY 则代表不需要其他数据了
            writeCellData.setType(CellDataTypeEnum.STRING);
            writeCellData.setStringValue("额外的放一些文字");

            List<ImageData> imageDatas = new ArrayList<>();
            ImageData  imageData = new ImageData();
            imageDatas.add(imageData);
            writeCellData.setImageDataList(imageDatas);
            // 放入2进制图片
            imageData.setImage(FileUtils.readFileToByteArray(new File(imagePath)));
            // 图片类型
            imageData.setImageType(ImageData.ImageType.PICTURE_TYPE_PNG);
            // 上 右 下 左 需要留空
            // 这个类似于 css 的 margin
            // 这里实测 不能设置太大 超过单元格原始大小后 打开会提示修复。暂时未找到很好的解法。
            imageData.setTop(5);
            imageData.setRight(40);
            imageData.setBottom(5);
            imageData.setLeft(5);

            // 放入第二个图片
            imageData = new ImageData();
            imageDatas.add(imageData);
            imageData.setImage(FileUtils.readFileToByteArray(new File(imagePath)));
            imageData.setImageType(ImageData.ImageType.PICTURE_TYPE_PNG);

            imageData.setTop(5);
            imageData.setRight(5);
            imageData.setBottom(5);
            imageData.setLeft(50);
            // 设置图片的位置 假设 现在目标 是 覆盖 当前单元格 和当前单元格右边的单元格
            // 起点相对于当前单元格为0 当然可以不写
            imageData.setRelativeFirstRowIndex(0);
            imageData.setRelativeFirstColumnIndex(0);
            imageData.setRelativeLastRowIndex(0);
            // 前面3个可以不写  下面这个需要写 也就是 结尾 需要相对当前单元格 往右移动一格
            // 也就是说 这个图片会覆盖当前单元格和 后面的那一格
            imageData.setRelativeLastColumnIndex(1);

            // 写入数据
            EasyExcel.write(fileName, ImageDTO.class).sheet().doWrite(list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
