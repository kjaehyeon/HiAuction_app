module.exports = (pool) => {
    const express = require('express');
    const router = express.Router();

    router.get('/items', async (req, res) => {
        const {query: {category_id, address_id}} = req
        let conn = null;

        try {
            conn = await pool.getConnection(async conn => conn);
            const [result] = await conn.query('SELECT It_id, Name, Quick_price,'
                                            + ' Current_price, Create_date, Img'
                                            + ' FROM ITEM'
                                            + ' WHERE c_id = ?'
                                            + ' AND Ad_id = ?'
                                            + ' ORDER BY Create_date DESC', [category_id, address_id]);
            const item_list = result.map((item_info) => {
                return {
                    item_id: item_info.It_id,
                    name: item_info.name,
                    current_price: item_info.Current_price,
                    immediate_price: item_info.Quick_price,
                    created_date: item_info.Create_date.toLocaleDateString(),
                    img_url: item_info.Img
                }
            });
            res.status(200).json({
                item_list
            });
        } catch (err) {
            res.status(500).json({
                message: err.message
            });
        } finally {
            conn.release();
        }
    });

    router.get('/item', async (req, res) => {
        const {query: {item_id}} = req;
        let conn = null;

        try { 
            conn = await pool.getConnection(async conn => conn);
            const [result] = await conn.query('SELECT M.U_id, M.Name User_name, A.Name Address,'
                                            + ' (SELECT ROUND(AVG(Score), 1) FROM RATING'
                                            + ' WHERE S_id = I.U_id) Score,'
                                            + ' I.It_id, I.Quick_price, I.Current_price,'
                                            + ' I.Create_date, I.Expire_date,'
                                            + ' I.Description, I.Img'
                                            + ' FROM ITEM I, MEMBER M, ADDRESS A'
                                            + ' WHERE I.U_id = M.U_id'
                                            + ' AND I.Ad_id = A.Ad_id'
                                            + ' AND I.It_id = ?', [item_id]);
            
            res.status(200).json({
                seller_id: result[0].U_id,
                seller_name: result[0].User_name,
                seller_rate: !result[0].Score ? 0 : result[0].Score,
                item_id: result[0].It_id,
                immediate_price: result[0].Quick_price,
                current_price: result[0].Current_price,
                created_date: result[0].Create_date,
                expired_date: result[0].Expire_date,
                description: result[0].Description,
                img_url: result[0].Img
            });
        } catch (err) {
            res.status(500).json({
                message: err.message
            });
        } finally {
            conn.release();
        }
    });

    router.post('/bid', async (req, res) => {
        const {body: {user_id, price, item_id}} = req;
        let conn = null;

        try {
            conn = await pool.getConnection(async conn => conn);
            const [result] = await conn.query('SELECT Min_bid_unit, Current_price,'
                                            + ' Is_end, Quick_price, U_id'
                                            + ' FROM ITEM'
                                            + ' WHERE It_id = ?', [item_id]);
            const {
                Min_bid_unit: min_bid_unit,
                Current_price: current_price,
                Is_end: is_end,
                Quick_price: quick_price,
                U_id: seller_id
            } = result[0];

            if (is_end !== '0') {
                res.status(400).json({
                    message: '유효하지 않은 상품입니다'
                });
            } else if (user_id === seller_id) {
                res.status(400).json({
                    message: '자신의 상품은 입찰할 수 없습니다'
                });
            } else if (price <= current_price) {
                res.status(400).json({
                    message: '입찰가는 현재가보다 커야합니다'
                });
            } else if (price >= quick_price) {
                res.status(400).json({
                    message: '즉시구매 해주세요'
                });
            } else if ((price - current_price) % min_bid_unit !== 0) {
                res.status(400).json({
                    message: '최소입찰단위를 맞춰주세요'
                });
            } else {
                await conn.query('INSERT INTO BID(Price, U_id, It_id)'
                                + ' VALUES(?, ?, ?)', [price, user_id, item_id]);
                await conn.query('UPDATE ITEM'
                                + ' SET Current_price = ?'
                                + ' WHERE It_id = ?', [price, item_id]);
                res.status(200).json({
                    message: 'accepted'
                });
            }
        } catch (err) {
            res.status(500).json({
                message: err.message
            });
        } finally {
            conn.release();
        }
    });

    return router;
};