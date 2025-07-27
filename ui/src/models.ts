
export interface Stock {
    ticker: string,
    previousDayMove: number,
    reasonForSelection: string,
}

export interface DayStocksView {
    stocks: [Stock],
    tradeDate: string
}

export interface UserPick {
    pick: number | null
}

export interface UserDetails {
    userName: string,
    currentAccountBalance: number
    updatedTradeDate: string
}
