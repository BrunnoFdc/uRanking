package ga.brunnofdc.uranking.routines.progress;

import ga.brunnofdc.uranking.economy.EconomicUnit;
import ga.brunnofdc.uranking.exceptions.MaxRankException;
import ga.brunnofdc.uranking.fixture.RankFixture;
import ga.brunnofdc.uranking.ranking.Rank;
import ga.brunnofdc.uranking.ranking.RankUtils;
import ga.brunnofdc.uranking.ranking.RankedPlayer;
import ga.brunnofdc.uranking.uRanking;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({uRanking.class, RankUtils.class})
public class ComputeProgressPercentageRoutineTest {

    @InjectMocks
    private ComputeProgressPercentageRoutine tested;

    private EconomicUnit unit;
    private uRanking plugin;
    private Player player;
    private RankedPlayer rankedPlayer;
    private Rank actualRank;
    private RankFixture nextRankFixture;

    @Before
    public void setUp() {
        mockStatic(uRanking.class);
        mockStatic(RankUtils.class);
        unit = mock(EconomicUnit.class);
        plugin = mock(uRanking.class);
        player = mock(Player.class);
        rankedPlayer = mock(RankedPlayer.class);
        actualRank = RankFixture.init().get();
        
        nextRankFixture = RankFixture.init().random().price(5000.0);

        when(plugin.getEconomicUnit())
                .thenReturn(unit);

        when(rankedPlayer.getPlayer())
                .thenReturn(player);

        when(rankedPlayer.getRank())
                .thenReturn(actualRank);

        PowerMockito.when(uRanking.getInstance())
                .thenReturn(plugin);
    }

    @Test
    public void ok() throws MaxRankException {
        //arrange
        final Rank nextRank = nextRankFixture.get();
        final Integer expected = 24;

        PowerMockito.when(RankUtils.getNextRank(eq(actualRank)))
                .thenReturn(nextRank);

        when(unit.getBalance(player))
                .thenReturn(1200.0);

        //act
        final Integer actual = tested.compute(rankedPlayer);

        //assert
        assertEquals(expected, actual);

        verifyStatic();
        uRanking.getInstance();

        verify(plugin).getEconomicUnit();
        verify(rankedPlayer).getPlayer();
        verify(rankedPlayer).getRank();
        verify(unit).getBalance(rankedPlayer.getPlayer());

        verifyStatic();
        RankUtils.getNextRank(rankedPlayer.getRank());
    }

    @Test
    public void shouldReturnNullWhenMaxRank() throws MaxRankException {
        //arrange
        PowerMockito.when(RankUtils.getNextRank(eq(actualRank)))
                .thenThrow(MaxRankException.class);

        //act
        final Integer actual = tested.compute(rankedPlayer);

        //assert
        assertNull(actual);
    }

    @Test
    public void shouldReturn100WhenPlayerBalanceIsHigherThanPrice() throws MaxRankException {
        //arrange
        final Rank nextRank = nextRankFixture.get();
        final Integer expected = 100;

        PowerMockito.when(RankUtils.getNextRank(eq(actualRank)))
                .thenReturn(nextRank);

        when(unit.getBalance(player))
                .thenReturn(6000.0);

        //act
        final Integer actual = tested.compute(rankedPlayer);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    public void shouldRoundUpWhenPercentageDecimalPlaceIsGreatherThan5() throws MaxRankException {
        //arrange
        final Rank nextRank = nextRankFixture.get();
        final Integer expected = 26;

        PowerMockito.when(RankUtils.getNextRank(eq(actualRank)))
                .thenReturn(nextRank);

        when(unit.getBalance(player))
                .thenReturn(1280.0);

        //act
        final Integer actual = tested.compute(rankedPlayer);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    public void shouldRoundDownWhenPercentageDecimalPlaceIsLessThan5() throws MaxRankException {
        //arrange
        final Rank nextRank = nextRankFixture.get();
        final Integer expected = 24;

        PowerMockito.when(RankUtils.getNextRank(eq(actualRank)))
                .thenReturn(nextRank);

        when(unit.getBalance(player))
                .thenReturn(1220.0);

        //act
        final Integer actual = tested.compute(rankedPlayer);

        //assert
        assertEquals(expected, actual);
    }
}