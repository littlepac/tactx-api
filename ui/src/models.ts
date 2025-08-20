
export interface Stock {
    id: number,
    ticker: string,
    name: string,
    previousOpen: number,
    previousClose: number,
    reasonForSelection: string,
}

export interface DayStocksView {
    stocks: [Stock],
    tradeDate: string
    tradeable: boolean
    comments: [Comment]
}

export interface Comment {
    id: number | null,
    comment: string | null
}

export interface LeaderBoard {
    top10: [LeaderBoardUser]
}

export interface LeaderBoardUser {
    userName: string,
    currentBalance: number
    lastTradedTicker: string | null
    lastIncrement: number
}

export interface UserDetails {
    userName: string,
    currentAccountBalance: number
    updatedTradeDate: string
}

export interface UserPick {
    pickId: number | null
}
