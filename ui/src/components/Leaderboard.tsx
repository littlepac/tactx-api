import React from 'react';
import { Card } from './ui/card';
import { Button } from './ui/button';
import { Badge } from './ui/badge';
import { Separator } from './ui/separator';
import { ArrowLeft, Trophy, Medal, Award } from 'lucide-react';
import { HundredBucksLogo } from './HundredBucksLogo';

interface Player {
  id: string;
  username: string;
  balance: number;
  monthlyReturn: number;
  monthlyReturnPercent: number;
  rank: number;
  lastTrade?: string;
}

// Mock leaderboard data with more players
const mockAllPlayers: Player[] = [
  {
    id: '1',
    username: 'StockMaster',
    balance: 156.78,
    monthlyReturn: 56.78,
    monthlyReturnPercent: 56.78,
    rank: 1,
    lastTrade: 'NVDA'
  },
  {
    id: '2', 
    username: 'TradingPro',
    balance: 142.35,
    monthlyReturn: 42.35,
    monthlyReturnPercent: 42.35,
    rank: 2,
    lastTrade: 'AAPL'
  },
  {
    id: '3',
    username: 'MarketWiz',
    balance: 138.92,
    monthlyReturn: 38.92,
    monthlyReturnPercent: 38.92,
    rank: 3,
    lastTrade: 'TSLA'
  },
  {
    id: '4',
    username: 'BullRun',
    balance: 125.67,
    monthlyReturn: 25.67,
    monthlyReturnPercent: 25.67,
    rank: 4,
    lastTrade: 'GOOGL'
  },
  {
    id: '5',
    username: 'DiamondHands',
    balance: 118.44,
    monthlyReturn: 18.44,
    monthlyReturnPercent: 18.44,
    rank: 5,
    lastTrade: 'Skip'
  },
  {
    id: '6',
    username: 'InvestorJoe',
    balance: 109.23,
    monthlyReturn: 9.23,
    monthlyReturnPercent: 9.23,
    rank: 6,
    lastTrade: 'AAPL'
  },
  {
    id: '7',
    username: 'QuickTrade',
    balance: 105.78,
    monthlyReturn: 5.78,
    monthlyReturnPercent: 5.78,
    rank: 7,
    lastTrade: 'TSLA'
  },
  {
    id: '8',
    username: 'SafePlayer',
    balance: 103.45,
    monthlyReturn: 3.45,
    monthlyReturnPercent: 3.45,
    rank: 8,
    lastTrade: 'Skip'
  },
  {
    id: '9',
    username: 'RiskyBiz',
    balance: 101.22,
    monthlyReturn: 1.22,
    monthlyReturnPercent: 1.22,
    rank: 9,
    lastTrade: 'GOOGL'
  },
  {
    id: '10',
    username: 'CautiousTrader',
    balance: 100.89,
    monthlyReturn: 0.89,
    monthlyReturnPercent: 0.89,
    rank: 10,
    lastTrade: 'AAPL'
  },
  {
    id: '11',
    username: 'NewbieTrade',
    balance: 99.45,
    monthlyReturn: -0.55,
    monthlyReturnPercent: -0.55,
    rank: 11,
    lastTrade: 'TSLA'
  },
  {
    id: '12',
    username: 'You',
    balance: 95.78,
    monthlyReturn: -4.22,
    monthlyReturnPercent: -4.22,
    rank: 12,
    lastTrade: 'NVDA'
  }
];

interface LeaderboardProps {
  onBack: () => void;
}

export function Leaderboard({ onBack }: LeaderboardProps) {
  const getRankIcon = (rank: number) => {
    switch (rank) {
      case 1:
        return <Trophy className="w-5 h-5 text-yellow-500" />;
      case 2:
        return <Medal className="w-5 h-5 text-gray-400" />;
      case 3:
        return <Award className="w-5 h-5 text-amber-600" />;
      default:
        return <span className="w-5 h-5 flex items-center justify-center text-sm font-medium text-muted-foreground">#{rank}</span>;
    }
  };

  // Get top 10 players
  const top10Players = mockAllPlayers.slice(0, 10);
  
  // Find current user
  const currentUser = mockAllPlayers.find(player => player.username === 'You');
  const isUserInTop10 = currentUser && currentUser.rank <= 10;
  
  // Calculate median balance
  const sortedBalances = [...mockAllPlayers].sort((a, b) => a.balance - b.balance);
  const medianBalance = sortedBalances.length % 2 === 0
    ? (sortedBalances[sortedBalances.length / 2 - 1].balance + sortedBalances[sortedBalances.length / 2].balance) / 2
    : sortedBalances[Math.floor(sortedBalances.length / 2)].balance;

  const currentMonth = new Date().toLocaleDateString('en-US', { 
    month: 'long', 
    year: 'numeric' 
  });

  return (
    <div className="min-h-screen bg-background p-4">
      <div className="w-full max-w-2xl mx-auto space-y-6">
        {/* Header */}
        <div className="flex items-center gap-4">
          <Button variant="ghost" size="sm" onClick={onBack} className="p-2">
            <ArrowLeft className="w-4 h-4" />
          </Button>
          <div className="flex-1 text-center">
            <HundredBucksLogo className="justify-center mb-2" />
            <p className="text-sm text-muted-foreground">{currentMonth} Leaderboard</p>
          </div>
          <div className="w-10"></div> {/* Spacer for centering */}
        </div>

        {/* Stats overview */}
        <div className="grid grid-cols-3 gap-4">
          <Card className="p-4 text-center">
            <div className="text-2xl font-medium text-primary">{mockAllPlayers.length}</div>
            <div className="text-xs text-muted-foreground">Total Players</div>
          </Card>
          <Card className="p-4 text-center">
            <div className="text-2xl font-medium text-green-600">${mockAllPlayers[0].balance.toFixed(2)}</div>
            <div className="text-xs text-muted-foreground">Top Balance</div>
          </Card>
          <Card className="p-4 text-center">
            <div className="text-2xl font-medium text-blue-600">${medianBalance.toFixed(2)}</div>
            <div className="text-xs text-muted-foreground">Median Balance</div>
          </Card>
        </div>

        {/* Top 10 Leaderboard */}
        <div className="space-y-3">
          <h2 className="text-lg font-medium text-center">Top 10</h2>
          {top10Players.map((player) => (
            <Card 
              key={player.id} 
              className={`p-4 ${player.username === 'You' ? 'border-primary/50 bg-primary/5' : ''}`}
            >
              <div className="flex items-center gap-4">
                {/* Rank icon */}
                <div className="flex-shrink-0">
                  {getRankIcon(player.rank)}
                </div>

                {/* Player info */}
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2">
                    <span className="font-medium truncate">
                      {player.username}
                      {player.username === 'You' && (
                        <Badge variant="outline" className="ml-2 text-xs">
                          You
                        </Badge>
                      )}
                    </span>
                  </div>
                  <div className="flex items-center gap-3 text-sm text-muted-foreground">
                    <span>Last: {player.lastTrade}</span>
                  </div>
                </div>

                {/* Performance */}
                <div className="text-right space-y-1">
                  <div className="font-medium">${player.balance.toFixed(2)}</div>
                  <div className={`text-sm ${player.monthlyReturn >= 0 ? 'text-green-600' : 'text-red-600'}`}>
                    {player.monthlyReturn >= 0 ? '+' : ''}${player.monthlyReturn.toFixed(2)}
                  </div>
                </div>
              </div>
            </Card>
          ))}

          {/* User's rank if not in top 10 */}
          {!isUserInTop10 && currentUser && (
            <>
              <Separator className="my-6" />
              <Card className="p-4 border-primary/50 bg-primary/5">
                <div className="flex items-center gap-4">
                  {/* Rank icon */}
                  <div className="flex-shrink-0">
                    {getRankIcon(currentUser.rank)}
                  </div>

                  {/* Player info */}
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center gap-2">
                      <span className="font-medium truncate">
                        {currentUser.username}
                        <Badge variant="outline" className="ml-2 text-xs">
                          You
                        </Badge>
                      </span>
                    </div>
                    <div className="flex items-center gap-3 text-sm text-muted-foreground">
                      <span>Last: {currentUser.lastTrade}</span>
                    </div>
                  </div>

                  {/* Performance */}
                  <div className="text-right space-y-1">
                    <div className="font-medium">${currentUser.balance.toFixed(2)}</div>
                    <div className={`text-sm ${currentUser.monthlyReturn >= 0 ? 'text-green-600' : 'text-red-600'}`}>
                      {currentUser.monthlyReturn >= 0 ? '+' : ''}${currentUser.monthlyReturn.toFixed(2)}
                    </div>
                  </div>
                </div>
              </Card>
            </>
          )}
        </div>

        {/* Footer info */}
        <div className="text-center text-xs text-muted-foreground space-y-1">
          <p>Rankings update after market close each day</p>
          <p>Balances reset to $100 at the start of each month</p>
        </div>
      </div>
    </div>
  );
}