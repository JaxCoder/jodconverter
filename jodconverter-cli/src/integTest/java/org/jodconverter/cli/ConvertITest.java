/*
 * Copyright 2004 - 2012 Mirko Nasato and contributors
 *           2016 - 2017 Simon Braconnier and contributors
 *
 * This file is part of JODConverter - Java OpenDocument Converter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jodconverter.cli;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests the {@link Convert} class, which contains the main function of the cli module.
 */
public class ConvertITest {

  private static final String CONFIG_DIR = "src/integTest/resources/config/";
  private static final String SOURCE_FILE = "src/integTest/resources/documents/test1.doc";
  private static final String OUTPUT_DIR = "test-output/" + ConvertITest.class.getSimpleName();

  /** Ensures we start with a fresh output directory. */
  @BeforeClass
  public static void createOutputDir() {

    // Ensure we start with a fresh output directory
    final File outputDir = new File(OUTPUT_DIR);
    FileUtils.deleteQuietly(outputDir);
    outputDir.mkdirs();
  }

  /**  Deletes the output directory. */
  @AfterClass
  public static void deleteOutputDir() {

    // Delete the output directory
    FileUtils.deleteQuietly(new File(OUTPUT_DIR));
  }

  @Test
  public void convert_WithFilenames_ShouldSucceed() throws Exception {

    final File inputFile = new File(SOURCE_FILE);
    final File outputFile = new File(OUTPUT_DIR, "convert.pdf");

    assertThat(outputFile).doesNotExist();

    Convert.main(new String[] {inputFile.getPath(), outputFile.getPath()});

    assertThat(outputFile).isFile();
    assertThat(outputFile.length()).isGreaterThan(0L);
  }

  @Test
  public void convert_WithOutputFormat_ShouldSucceed() throws Exception {

    final File inputFile = new File(SOURCE_FILE);
    final File outputFile =
        new File(
            inputFile.getParentFile(), FilenameUtils.getBaseName(inputFile.getName()) + ".pdf");

    assertThat(outputFile).doesNotExist();

    Convert.main(new String[] {"-f", "pdf", inputFile.getPath()});

    assertThat(outputFile).isFile();
    assertThat(outputFile.length()).isGreaterThan(0L);

    FileUtils.deleteQuietly(outputFile); // Prevent further test failure.
  }

  @Test
  public void convert_WithMultipleFilters_ShouldSucceed() throws Exception {

    final File filterChainFile = new File(CONFIG_DIR + "applicationContext_multipleFilters.xml");
    final File inputFile = new File(SOURCE_FILE);
    final File outputFile = new File(OUTPUT_DIR, "convert_WithMultipleFilters.pdf");

    assertThat(outputFile).doesNotExist();

    Convert.main(
        new String[] {"-a", filterChainFile.getPath(), inputFile.getPath(), outputFile.getPath()});

    assertThat(outputFile).isFile();
    assertThat(outputFile.length()).isGreaterThan(0L);
  }

  @Test
  public void convert_WithSingleFilter_ShouldSucceed() throws Exception {

    final File filterChainFile = new File(CONFIG_DIR + "applicationContext_singleFilter.xml");
    final File inputFile = new File(SOURCE_FILE);
    final File outputFile = new File(OUTPUT_DIR, "convert_WithSingleFilter.pdf");

    assertThat(outputFile).doesNotExist();

    Convert.main(
        new String[] {"-a", filterChainFile.getPath(), inputFile.getPath(), outputFile.getPath()});

    assertThat(outputFile).isFile();
    assertThat(outputFile.length()).isGreaterThan(0L);
  }
}
