package org.cempaka.cyclone.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.cempaka.cyclone.beans.Parcel;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileParcelRepositoryTest
{
    private static final UUID PARCEL_ID = UUID.randomUUID();
    private static final byte[] PARCEL_DATA = "test".getBytes();
    private static Path storagePath;
    private static Path parcelPath;

    private FileParcelRepository fileParcelRepository;

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        storagePath = Files.createTempDirectory("storm_test");
        parcelPath = Paths.get(storagePath.toString(), PARCEL_ID.toString());
    }

    @Before
    public void setUp()
    {
        fileParcelRepository = new FileParcelRepository(storagePath.toString());
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
        Files.deleteIfExists(storagePath);
    }

    @Test
    public void shouldFailForNullInitialization()
    {
        assertThatThrownBy(() -> new FileParcelRepository(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldFailForNullParameters()
    {
        assertThatThrownBy(() -> fileParcelRepository.delete(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fileParcelRepository.get(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> fileParcelRepository.put(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldPutParcel() throws IOException
    {
        //given
        final Parcel parcel = Parcel.of(PARCEL_ID, PARCEL_DATA);
        //when
        fileParcelRepository.put(parcel);
        //then
        final byte[] data = Files.readAllBytes(parcelPath);
        assertThat(data).isEqualTo(PARCEL_DATA);
    }

    @Test
    public void shouldOverrideParcel() throws IOException
    {
        //given
        Files.write(parcelPath, PARCEL_DATA);
        final byte[] newData = {0};
        final Parcel parcel = Parcel.of(PARCEL_ID, newData);
        //when
        fileParcelRepository.put(parcel);
        //then
        final byte[] data = Files.readAllBytes(parcelPath);
        assertThat(data).isEqualTo(newData);
    }

    @Test
    public void shouldGetParcel() throws IOException
    {
        //given
        Files.write(parcelPath, PARCEL_DATA);
        //when
        final Parcel parcel = fileParcelRepository.get(PARCEL_ID);
        //then
        assertThat(parcel).isNotNull();
        assertThat(parcel.getId()).isEqualTo(PARCEL_ID);
        assertThat(parcel.getData()).isEqualTo(PARCEL_DATA);
    }

    @Test
    public void shouldReturnNullOnNotExistingParcel()
    {
        //given
        final UUID uuid = UUID.randomUUID();
        //when
        final Parcel parcel = fileParcelRepository.get(uuid);
        //then
        assertThat(parcel).isNull();
    }

    @Test
    public void shouldDeleteParcel() throws IOException
    {
        //given
        Files.write(parcelPath, PARCEL_DATA);
        //when
        fileParcelRepository.delete(PARCEL_ID);
        //then
        final boolean exists = Files.exists(parcelPath);
        assertThat(exists).isFalse();
    }

    @Test
    public void shouldListParcels() throws IOException
    {
        //given
        Files.write(parcelPath, PARCEL_DATA);
        //when
        final List<UUID> parcels = fileParcelRepository.list().collect(Collectors.toList());
        //then
        assertThat(parcels).containsOnly(PARCEL_ID);
    }
}