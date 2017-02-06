package com.vagnerbarbosa.termometro.vendas.web.managedbeans;

/**
 *
 * @author vagner
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.vagnerbarbosa.termometro.vendas.web.model.Sales;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.chartistjsf.model.chart.AspectRatio;
import org.chartistjsf.model.chart.Axis;
import org.chartistjsf.model.chart.AxisType;
import org.chartistjsf.model.chart.BarChartModel;
import org.chartistjsf.model.chart.BarChartSeries;

@ManagedBean
@ViewScoped
public class BarChartDataBean implements Serializable {

    private BarChartModel barChartModel;
    ObjectMapper mapper = new ObjectMapper();
    
    List<Sales> vendas = new ArrayList<>();

    public BarChartDataBean() throws IOException {
        createBarModel();
    }

    public void createBarModel() throws IOException {
        barChartModel = new BarChartModel();
        barChartModel.setAspectRatio(AspectRatio.MAJOR_ELEVENTH);

        BarChartSeries series1 = new BarChartSeries();
        series1.setName("R$");

        for (int i = 0; i < this.getVendas().size(); i++) {
            barChartModel.addLabel("F" + this.getVendas().get(i).getBranchNumber());
            series1.set(this.getVendas().get(i).getBalanceTotal());
        }
        barChartModel.addSeries(series1);

        Axis xAxis = barChartModel.getAxis(AxisType.X);
        xAxis.setShowGrid(false);

        barChartModel.setShowTooltip(true);
        barChartModel.setSeriesBarDistance(15);
        barChartModel.setAnimatePath(true);
    }

    /**
     * @return the barChartModel
     */
    public BarChartModel getBarChartModel() {
        return barChartModel;
    }

    /**
     * @param barChartModel the barChartModel to set
     */
    public void setBarChartModel(BarChartModel barChartModel) {
        this.barChartModel = barChartModel;
    }
    
    public List<Sales> atualizaVendas() throws IOException {        
        Client c = Client.create();
        WebResource wr = c.resource("http://192.168.19.250:8080/sales-weather/webservice/sales/");
        String json = wr.get(String.class);
        this.vendas = null;
        this.vendas = (List<Sales>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("salesList", mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, Sales.class)));
        return vendas;
    }    

    public List<Sales> getVendas() throws IOException {
        this.vendas = (List<Sales>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("salesList");
        return vendas;
    }

}
