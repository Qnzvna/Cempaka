package org.cempaka.cyclone.storage.repositories;

import static org.cempaka.cyclone.utils.Preconditions.checkNotNull;

import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cempaka.cyclone.jooq.Tables;
import org.cempaka.cyclone.jooq.tables.records.MetadataRecord;
import org.jooq.DSLContext;

@Singleton
public class JooqMetadataRepository implements MetadataRepository
{
    private final DSLContext context;

    @Inject
    public JooqMetadataRepository(final DSLContext context)
    {
        this.context = checkNotNull(context);
    }

    @Override
    public void put(final MetadataRecord metadataRecord)
    {
        context.transaction(configuration -> {
            final DSLContext context = configuration.dsl();
            final Optional<MetadataRecord> currentRecord = get(context, metadataRecord.getMetadataId());
            if (currentRecord.isPresent()) {
                context.update(Tables.METADATA)
                    .set(Tables.METADATA.VALUE, metadataRecord.getValue())
                    .where(Tables.METADATA.METADATA_ID.eq(metadataRecord.getMetadataId()))
                    .execute();
            } else {
                context.insertInto(Tables.METADATA).values(metadataRecord).execute();
            }
        });
    }

    @Override
    public Optional<MetadataRecord> get(final String metadataId)
    {
        checkNotNull(metadataId);
        return get(context, metadataId);
    }

    private Optional<MetadataRecord> get(final DSLContext context, final String metadataId)
    {
        return Optional.ofNullable(
            context.selectFrom(Tables.METADATA).where(Tables.METADATA.METADATA_ID.eq(metadataId)).fetchOne()
        );
    }

    @Override
    public void delete(final String metadataId)
    {
        checkNotNull(metadataId);
        context.deleteFrom(Tables.METADATA).where(Tables.METADATA.METADATA_ID.eq(metadataId)).execute();
    }
}
