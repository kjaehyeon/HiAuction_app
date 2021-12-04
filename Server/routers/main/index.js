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

    return router;
};