package com.vit.common.utils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by wq on 2016/7/5.
 * 图片压缩工具
 */
public class ImageUtils {
    /**
     * 等比例压缩图片
     *
     * @param srcBufferedImage 原图
     * @param targetWidth      目标图片最大宽度
     * @return
     * @throws IOException
     */
    public byte[] resize(BufferedImage srcBufferedImage, int targetWidth, int targetHeight) throws IOException {
        Image resizedImage = null;
        // 原始图片长宽
        int iWidth = srcBufferedImage.getWidth();
        int iHeight = srcBufferedImage.getHeight();
        int multiple = 1;
        if (iWidth > targetWidth) {
            multiple = iWidth / targetWidth + 1;
        } else if (iHeight > targetHeight) {
            multiple = iHeight / targetHeight + 1;
        } else {
            multiple = 2;
        }
        resizedImage = srcBufferedImage.getScaledInstance(iWidth / multiple,
                iHeight / multiple, Image.SCALE_SMOOTH);

        // This code ensures that all the pixels in the image are loaded.
        Image temp = new ImageIcon(resizedImage).getImage();

        // Create the buffered image.
        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null),
                temp.getHeight(null), BufferedImage.TYPE_INT_RGB);

        // Copy image to buffered image.
        Graphics g = bufferedImage.createGraphics();

        // Clear background and paint the image.
        g.setColor(Color.white);
        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
        g.drawImage(temp, 0, 0, null);
        g.dispose();

        // Soften.
        float softenFactor = 0.05f;
        float[] softenArray = {0, softenFactor, 0, softenFactor,
                1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0};
        Kernel kernel = new Kernel(3, 3, softenArray);
        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        bufferedImage = cOp.filter(bufferedImage, null);

        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(1.0F); // Highest quality
        // Write the JPEG to our ByteArray stream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageOutputStream imageOutputStream = ImageIO
                .createImageOutputStream(byteArrayOutputStream);
        writer.setOutput(imageOutputStream);
        writer.write(null, new IIOImage(bufferedImage, null, null), param);

        imageOutputStream.close();
        byteArrayOutputStream.close();
        writer.dispose();

        return byteArrayOutputStream.toByteArray();
    }
}
