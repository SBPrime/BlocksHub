package org.primesoft.blockshub.api.platform;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class TypedOnlyBlockTests {

    @Test
    public void createBlockDataTest() {

        BlockData bd = mock(BlockData.class);

        Block block = TypeOnlyBlock.create(bd);

        Assert.assertNotNull(block);
    }
}
