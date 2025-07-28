
export interface Stock {
    id: number,
    ticker: string,
    previousDayMove: number,
    reasonForSelection: string,
}

export interface DayStocksView {
    stocks: [Stock],
    tradeDate: string
}

export interface UserDetails {
    userName: string,
    currentAccountBalance: number
    updatedTradeDate: string
}
