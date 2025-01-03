/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.imaging.formats.tiff;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;
import org.apache.commons.imaging.internal.Debug;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class TiffRoundtripTest extends TiffBaseTest {

    @Benchmark
    public void benchmark() throws Exception {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        final List<File> images = getTiffImages();
        for (final File imageFile : images) {

            Debug.debug("imageFile", imageFile);

            final ImageMetadata metadata = Imaging.getMetadata(imageFile);
            assertNotNull(metadata);

            final ImageInfo imageInfo = Imaging.getImageInfo(imageFile);
            assertNotNull(imageInfo);

            final BufferedImage image = Imaging.getBufferedImage(imageFile);
            assertNotNull(image);

            final TiffImageParser tiffImageParser = new TiffImageParser();

            final int[] compressions = {
                    TiffConstants.COMPRESSION_UNCOMPRESSED,
                    TiffConstants.COMPRESSION_LZW,
                    TiffConstants.COMPRESSION_PACKBITS,
                    TiffConstants.COMPRESSION_DEFLATE_ADOBE
            };
            executorService.submit(() -> {

                for (final int compression : compressions) {
                    try {
                        final byte[] tempFile;
                        final TiffImagingParameters params = new TiffImagingParameters();
                        params.setCompression(compression);

                        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                            tiffImageParser.writeImage(image, bos, params);
                            tempFile = bos.toByteArray();
                        }

                        final BufferedImage image2 = Imaging.getBufferedImage(tempFile);
                        assertNotNull(image2);
                    } catch (IOException e) {
                        // Handle exception as needed
                        e.printStackTrace();
                    }
                }
            });
        }
        // Shutdown the executor and wait for all tasks to complete
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    @Benchmark
    public void processUncompressed() throws Exception {
        int[] compressions = {TiffConstants.COMPRESSION_UNCOMPRESSED};
        runTest(compressions);
    }

    @Benchmark
    public void processLzw() throws Exception {
        int[] compressions = {TiffConstants.COMPRESSION_LZW};
        runTest(compressions);
    }

    @Benchmark
    public void processPackbits() throws Exception {
        int[] compressions = {TiffConstants.COMPRESSION_PACKBITS};
        runTest(compressions);
    }

    @Benchmark
    public void processDeflateAdobe() throws Exception {
        int[] compressions = {TiffConstants.COMPRESSION_DEFLATE_ADOBE};
        runTest(compressions);
    }

    private void runTest(int[] compressions) throws IOException, InterruptedException {
        final List<File> images = getTiffImages();


        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (final File imageFile : images) {

            Debug.debug("imageFile", imageFile);

            final ImageMetadata metadata = Imaging.getMetadata(imageFile);
            assertNotNull(metadata);

            final ImageInfo imageInfo = Imaging.getImageInfo(imageFile);
            assertNotNull(imageInfo);

            final BufferedImage image = Imaging.getBufferedImage(imageFile);
            assertNotNull(image);

            final TiffImageParser tiffImageParser = new TiffImageParser();
            executorService.submit(() -> {

                for (final int compression : compressions) {
                    try {
                        final byte[] tempFile;
                        final TiffImagingParameters params = new TiffImagingParameters();
                        params.setCompression(compression);

                        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                            tiffImageParser.writeImage(image, bos, params);
                            tempFile = bos.toByteArray();
                        }

                        final BufferedImage image2 = Imaging.getBufferedImage(tempFile);
                        assertNotNull(image2);
                    } catch (IOException e) {
                        // Handle exception as needed
                        e.printStackTrace();
                    }
                }
            });
        }
        // Shutdown the executor and wait for all tasks to complete
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }
}
